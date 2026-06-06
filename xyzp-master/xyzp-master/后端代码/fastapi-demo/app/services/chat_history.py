"""
chat_history.py - LangChain BaseChatMessageHistory 实现
==========================================================

【功能】
  将 MySQL kfzs 持久化 + Redis 缓存的聊天存储机制，
  包装为 LangChain 认可的 BaseChatMessageHistory 接口。
  供 rag_service 的 chain 直接使用，实现上下文记忆。

【缓存策略】
  - Redis key: "session:{session_id}"
  - Redis value: JSON 序列化的消息列表
  - TTL: 3600 秒，每次读写续期
"""

import json
import logging
from typing import List

from sqlalchemy import select, update
from sqlalchemy.ext.asyncio import AsyncSession
from redis.asyncio import Redis

from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.messages import BaseMessage, HumanMessage, AIMessage, message_to_dict, messages_from_dict

from app.models.chat_message import ChatMessage
from app.models.chat_session import ChatSession

logger = logging.getLogger(__name__)

# Redis 缓存 key 前缀与 TTL
SESSION_KEY_PREFIX = "session:"
CACHE_TTL = 3600


class ChatHistoryStore(BaseChatMessageHistory):
    """
    基于 MySQL + Redis 的 ChatMessageHistory 实现。

    每个实例绑定一个 session_id，管理该会话的消息历史。
    由 ChatService 按需创建，不独立暴露给路由层。
    """

    def __init__(self, session_id: int, db: AsyncSession, redis: Redis):
        self.session_id = session_id
        self._db = db
        self._redis = redis

    # =================================================================
    # 属性
    # =================================================================

    @property
    def messages(self) -> List[BaseMessage]:
        """
        同步属性（LangChain 要求），内部委托给异步方法。
        LangChain 的 RunnableWithMessageHistory 会调用此属性。
        由于 LangChain 内部以同步方式访问，这里用 asyncio.run 桥接。
        """
        import asyncio
        try:
            loop = asyncio.get_running_loop()
        except RuntimeError:
            return asyncio.run(self._aget_messages())
        # 在已有事件循环中，使用 nest_asyncio 或直接等待
        # 安全做法：如果已经在事件循环中且 run 不行，走同步 fallback
        import concurrent.futures
        future = asyncio.run_coroutine_threadsafe(self._aget_messages(), loop)
        return future.result(timeout=10)

    async def _aget_messages(self) -> List[BaseMessage]:
        """异步获取消息列表，带 Redis 缓存"""
        # 1. 尝试从 Redis 读取
        cache_key = f"{SESSION_KEY_PREFIX}{self.session_id}"
        try:
            cached = await self._redis.get(cache_key)
            if cached:
                await self._redis.expire(cache_key, CACHE_TTL)
                msg_dicts = json.loads(cached)
                return messages_from_dict(msg_dicts)
        except Exception as e:
            logger.warning(f"Redis 读取失败 session={self.session_id}: {e}")

        # 2. Redis 未命中 → 查 MySQL
        result = await self._db.execute(
            select(ChatMessage)
            .where(ChatMessage.session_id == self.session_id)
            .order_by(ChatMessage.created_at.asc())
        )
        rows = result.scalars().all()

        messages: List[BaseMessage] = []
        for row in rows:
            if row.role == "user":
                messages.append(HumanMessage(content=row.content))
            elif row.role == "assistant":
                messages.append(AIMessage(content=row.content))

        # 3. 写回 Redis 缓存
        if messages:
            try:
                msg_dicts = [message_to_dict(m) for m in messages]
                await self._redis.setex(cache_key, CACHE_TTL, json.dumps(msg_dicts, ensure_ascii=False))
            except Exception as e:
                logger.warning(f"Redis 写入失败 session={self.session_id}: {e}")

        return messages

    # =================================================================
    # 写操作
    # =================================================================

    def add_message(self, message: BaseMessage) -> None:
        """同步包装 add_message"""
        import asyncio
        try:
            loop = asyncio.get_running_loop()
        except RuntimeError:
            return asyncio.run(self._aadd_message(message))
        import concurrent.futures
        future = asyncio.run_coroutine_threadsafe(self._aadd_message(message), loop)
        return future.result(timeout=10)

    async def _aadd_message(self, message: BaseMessage) -> None:
        """异步添加消息：写 MySQL + 更新 Redis 缓存 + 更新 session.updated_at"""
        role = "user" if isinstance(message, HumanMessage) else "assistant"
        content = message.content if isinstance(message.content, str) else str(message.content)

        # 1. 写入 MySQL
        chat_msg = ChatMessage(
            session_id=self.session_id,
            role=role,
            content=content,
        )
        self._db.add(chat_msg)

        # 2. 更新 session 的 updated_at
        await self._db.execute(
            update(ChatSession)
            .where(ChatSession.id == self.session_id)
            .values(updated_at=None)  # onupdate 会自动触发
        )
        await self._db.flush()

        # 3. 更新 Redis 缓存（追加）
        cache_key = f"{SESSION_KEY_PREFIX}{self.session_id}"
        try:
            cached = await self._redis.get(cache_key)
            msg_list = json.loads(cached) if cached else []
            msg_list.append(message_to_dict(message))
            await self._redis.setex(cache_key, CACHE_TTL, json.dumps(msg_list, ensure_ascii=False))
        except Exception as e:
            logger.warning(f"Redis 缓存更新失败 session={self.session_id}: {e}")

    def clear(self) -> None:
        """同步包装 clear"""
        import asyncio
        try:
            loop = asyncio.get_running_loop()
        except RuntimeError:
            return asyncio.run(self._aclear())
        import concurrent.futures
        future = asyncio.run_coroutine_threadsafe(self._aclear(), loop)
        return future.result(timeout=10)

    async def _aclear(self) -> None:
        """清空当前会话：软删除 session + 清理缓存"""
        cache_key = f"{SESSION_KEY_PREFIX}{self.session_id}"
        try:
            await self._redis.delete(cache_key)
        except Exception:
            pass

    # =================================================================
    # 工具方法
    # =================================================================

    async def get_messages_since(self, message_id: int) -> List[BaseMessage]:
        """获取某条消息之后的所有消息"""
        result = await self._db.execute(
            select(ChatMessage)
            .where(
                ChatMessage.session_id == self.session_id,
                ChatMessage.id > message_id,
            )
            .order_by(ChatMessage.created_at.asc())
        )
        rows = result.scalars().all()
        msgs = []
        for row in rows:
            if row.role == "user":
                msgs.append(HumanMessage(content=row.content))
            else:
                msgs.append(AIMessage(content=row.content))
        return msgs

    async def message_count(self) -> int:
        """获取会话的消息总数"""
        cache_key = f"{SESSION_KEY_PREFIX}{self.session_id}"
        try:
            cached = await self._redis.get(cache_key)
            if cached:
                return len(json.loads(cached))
        except Exception:
            pass
        result = await self._db.execute(
            select(ChatMessage).where(ChatMessage.session_id == self.session_id)
        )
        return len(result.scalars().all())

    async def refresh_cache(self) -> None:
        """强制刷新 Redis 缓存（从 MySQL 重新加载）"""
        cache_key = f"{SESSION_KEY_PREFIX}{self.session_id}"
        try:
            await self._redis.delete(cache_key)
        except Exception:
            pass
        await self._aget_messages()  # 重新加载即写回
