"""
chat_service.py — 聊天业务编排层
===================================

【功能】
  会话/消息的 CRUD 管理 + Redis 缓存调度 + 对接 rag_service。
  流式生成器由路由层（chatrouter.py）订阅后包装为 SSE。

【关键方法】
  ask_temp(question: str, session_id: str=None) — AsyncGenerator[dict]
      临时会话流式问答，数据不落库。

  ask_session(session_id, question, user_id) — AsyncGenerator[dict]
      历史会话流式问答，数据持久化到 MySQL + Redis。

  list_sessions(user_id) → list[SessionSummary]
  get_session(session_id, user_id) → SessionDetailResponse
  delete_session(session_id, user_id) → None
"""

import json
import logging
import time
import random
import uuid
from typing import AsyncGenerator, Optional, List

from sqlalchemy import select, update, delete, func
from sqlalchemy.ext.asyncio import AsyncSession
from redis.asyncio import Redis

from langchain_core.messages import HumanMessage, AIMessage

from app.models.chat_session import ChatSession
from app.models.chat_message import ChatMessage
from app.services.rag_service import ask_stream
from app.services.chat_history import ChatHistoryStore, SESSION_KEY_PREFIX, CACHE_TTL

logger = logging.getLogger(__name__)

# 内存中的临时会话历史（tempAsk 用）
# key: session_id (UUID 字符串), value: list[BaseMessage]
_temp_memory: dict[str, list] = {}


def _generate_snowflake_id() -> int:
    """生成简化版雪花 ID（毫秒时间戳 + 随机序列号）"""
    ts = int(time.time() * 1000)
    seq = random.randint(0, 4095)
    return (ts << 12) | seq


def _generate_temp_session_id() -> str:
    """生成临时会话 UUID"""
    return str(uuid.uuid4())


# =================================================================
# 临时会话（不落库）
# =================================================================

async def ask_temp(
    question: str,
    session_id: Optional[str] = None,
) -> AsyncGenerator[dict, None]:
    """
    临时会话流式问答。

    数据不落库，上下文保存在内存中（_temp_memory）。

    Yields:
        {"type": "meta", "session_id": str}
        {"type": "token", "token": str}
        {"type": "done", "answer": str, "sources": [...]}
    """
    # 无 session_id 则新建
    if not session_id:
        session_id = _generate_temp_session_id()

    # 发送 meta 事件
    yield {"type": "meta", "session_id": session_id, "code": 200, "msg": None}

    # 获取历史消息
    history = _temp_memory.get(session_id, [])

    # 调用 RAG 流式生成
    full_answer = ""
    sources = []
    async for event in ask_stream(question=question, chat_history=history):
        if event["type"] == "token":
            full_answer += event["token"]
            yield event
        elif event["type"] == "done":
            full_answer = event["answer"]
            sources = event["sources"]

    # 保存对话到内存
    if session_id not in _temp_memory:
        _temp_memory[session_id] = []
    _temp_memory[session_id].append(HumanMessage(content=question))
    _temp_memory[session_id].append(AIMessage(content=full_answer))

    # 发送 done 事件
    yield {"type": "done", "answer": full_answer, "sources": sources}


# =================================================================
# 历史会话（持久化）
# =================================================================

async def create_session(db: AsyncSession, user_id: int, title: str = "新会话") -> ChatSession:
    """创建新会话行"""
    session = ChatSession(
        id=_generate_snowflake_id(),
        user_id=user_id,
        title=title[:100] if title else "新会话",
    )
    db.add(session)
    await db.flush()
    await db.refresh(session)
    return session


async def ask_session(
    session_id: Optional[int],
    question: str,
    user_id: int,
    db: AsyncSession,
    redis: Redis,
) -> AsyncGenerator[dict, None]:
    """
    历史会话流式问答。

    流程：
      1. 无 session_id → 创建新会话 + 写用户消息
      2. 有 session_id → 校验归属
      3. 取历史 → RAG 流式生成 → 逐 token 产出
      4. 流结束后写入 AI 消息到 MySQL + Redis

    Yields:
        {"type": "meta", "session_id": int}
        {"type": "token", "token": str}
        {"type": "done", "answer": str, "sources": [...]}
    """
    # 1. 处理 session_id
    if not session_id:
        # 新建会话
        title = question[:30] + ("..." if len(question) > 30 else "")
        session = await create_session(db, user_id, title=title)
        session_id = session.id
    else:
        # 校验归属
        result = await db.execute(
            select(ChatSession).where(
                ChatSession.id == session_id,
                ChatSession.user_id == user_id,
            )
        )
        session = result.scalar_one_or_none()
        if not session:
            # session 不存在或不属于该用户，返回错误
            yield {"type": "meta", "code": 201, "msg": "会话不存在或无权访问", "session_id": session_id}
            yield {"type": "done", "answer": "", "sources": []}
            return

    # 2. 发送 meta 事件
    yield {"type": "meta", "code": 200, "msg": None, "session_id": session_id}

    # 3. 先写入用户消息到 MySQL
    user_msg = ChatMessage(session_id=session_id, role="user", content=question)
    db.add(user_msg)
    await db.flush()

    # 4. 获取历史消息（通过 ChatHistoryStore）
    store = ChatHistoryStore(session_id=session_id, db=db, redis=redis)
    try:
        history = store.messages  # 同步属性
    except Exception as e:
        logger.warning(f"获取历史消息失败 session={session_id}: {e}")
        history = []

    # 5. 调用 RAG 流式生成
    full_answer = ""
    sources = []
    try:
        async for event in ask_stream(question=question, chat_history=history):
            if event["type"] == "token":
                full_answer += event["token"]
                yield event
            elif event["type"] == "done":
                full_answer = event["answer"]
                sources = event["sources"]
    except Exception as e:
        logger.error(f"RAG 生成失败: {e}")
        full_answer = f"抱歉，生成回答时出现错误: {e}"

    # 6. 流结束后写入 AI 消息到 MySQL
    ai_msg = ChatMessage(
        session_id=session_id,
        role="assistant",
        content=full_answer,
        sources=json.dumps(sources, ensure_ascii=False) if sources else None,
    )
    db.add(ai_msg)
    await db.flush()

    # 7. 更新 Redis 缓存
    try:
        await store.refresh_cache()
    except Exception as e:
        logger.warning(f"刷新缓存失败 session={session_id}: {e}")

    # 8. 更新会话标题（如果还是默认的"新会话"）
    if session and session.title == "新会话":
        new_title = question[:30] + ("..." if len(question) > 30 else "")
        await db.execute(
            update(ChatSession)
            .where(ChatSession.id == session_id)
            .values(title=new_title)
        )

    # 9. 发送 done 事件
    yield {"type": "done", "answer": full_answer, "sources": sources}


# =================================================================
# 会话查询（同步 JSON 返回）
# =================================================================

async def list_sessions(db: AsyncSession, user_id: int) -> list[dict]:
    """查询用户的所有历史会话摘要列表，按更新时间倒序"""
    result = await db.execute(
        select(ChatSession)
        .where(
            ChatSession.user_id == user_id,
            ChatSession.status == 1,
        )
        .order_by(ChatSession.updated_at.desc())
    )
    sessions = result.scalars().all()

    summaries = []
    for s in sessions:
        # 统计消息数
        cnt_result = await db.execute(
            select(func.count(ChatMessage.id)).where(ChatMessage.session_id == s.id)
        )
        msg_count = cnt_result.scalar() or 0

        summaries.append({
            "id": s.id,
            "title": s.title,
            "message_count": msg_count,
            "created_at": s.created_at.isoformat() if s.created_at else None,
            "updated_at": s.updated_at.isoformat() if s.updated_at else None,
        })

    return summaries


async def get_session_detail(
    db: AsyncSession,
    session_id: int,
    user_id: int,
) -> Optional[dict]:
    """查询单条会话详情（含完整消息列表）"""
    # 1. 查会话
    result = await db.execute(
        select(ChatSession).where(
            ChatSession.id == session_id,
            ChatSession.user_id == user_id,
            ChatSession.status == 1,
        )
    )
    session = result.scalar_one_or_none()
    if not session:
        return None

    # 2. 查消息
    msg_result = await db.execute(
        select(ChatMessage)
        .where(ChatMessage.session_id == session_id)
        .order_by(ChatMessage.created_at.asc())
    )
    messages = msg_result.scalars().all()

    message_items = []
    for m in messages:
        sources = []
        if m.sources:
            try:
                sources = json.loads(m.sources)
            except (json.JSONDecodeError, TypeError):
                sources = []

        message_items.append({
            "id": m.id,
            "role": m.role,
            "content": m.content,
            "sources": sources,
            "created_at": m.created_at.isoformat() if m.created_at else None,
        })

    return {
        "session": {
            "id": session.id,
            "title": session.title,
            "message_count": len(messages),
            "created_at": session.created_at.isoformat() if session.created_at else None,
            "updated_at": session.updated_at.isoformat() if session.updated_at else None,
        },
        "messages": message_items,
    }


async def delete_session(
    db: AsyncSession,
    redis: Redis,
    session_id: int,
    user_id: int,
) -> bool:
    """
    物理删除会话及其所有消息。

    Returns:
        True 删除成功，False 会话不存在或无权删除
    """
    # 1. 校验归属
    result = await db.execute(
        select(ChatSession).where(
            ChatSession.id == session_id,
            ChatSession.user_id == user_id,
        )
    )
    session = result.scalar_one_or_none()
    if not session:
        return False

    # 2. 物理删除消息
    await db.execute(
        delete(ChatMessage).where(ChatMessage.session_id == session_id)
    )

    # 3. 物理删除会话
    await db.execute(
        delete(ChatSession).where(ChatSession.id == session_id)
    )

    # 4. 清理 Redis 缓存
    try:
        await redis.delete(f"{SESSION_KEY_PREFIX}{session_id}")
    except Exception as e:
        logger.warning(f"Redis 清理失败 session={session_id}: {e}")

    await db.flush()
    return True
