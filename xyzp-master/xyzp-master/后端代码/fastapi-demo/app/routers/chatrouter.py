"""
chatrouter.py — 聊天 API 路由层
================================

【功能】
  遵循 Java controller 命名风格，只用 POST。
  SSE 端点通过 URL query 传参（EventSource 限制），JSON 端点通过 POST body 传参。

【接口】
  POST /chat/tempAsk          — 临时会话（免登录，SSE 流式）
  POST /chat/sendMessage      — 历史会话发消息（需登录，SSE 流式）
  POST /chat/searchSession    — 查询会话列表（需登录，JSON）
  POST /chat/getSession       — 查询会话详情（需登录，JSON）
  POST /chat/removeSession    — 删除会话（需登录，JSON）
"""

import json
import logging
from typing import Optional

from fastapi import APIRouter, Request, Depends, Query
from fastapi.responses import StreamingResponse
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.database import get_database
from app.core.redis import get_redis
from app.schemas.common import Result
from app.services import chat_service

logger = logging.getLogger(__name__)
router = APIRouter()


# =================================================================
# 辅助函数
# =================================================================

def _require_login(request: Request):
    """要求登录，返回 user_id 或报错"""
    if not getattr(request.state, "is_logged_in", False):
        return None
    uid = getattr(request.state, "user_id", None)
    if uid is not None:
        try:
            uid = int(uid)
        except (ValueError, TypeError):
            return None
    return uid


def _sse_event(event: str, data: dict) -> str:
    """格式化 SSE 事件"""
    return f"event: {event}\ndata: {json.dumps(data, ensure_ascii=False)}\n\n"


# =================================================================
# 1. 临时会话 — SSE 流式（免登录）
# =================================================================

@router.post("/tempAsk")
async def temp_ask(
    question: str = Query(..., min_length=1, max_length=2000, description="用户问题"),
    session_id: Optional[str] = Query(None, description="会话ID，续聊时传入"),
):
    """
    临时会话 AI 问答（SSE 流式响应）。

    数据不落库，上下文保存在服务端内存中。
    前端使用 EventSource，参数通过 URL query 传递。
    """

    async def event_generator():
        try:
            async for event in chat_service.ask_temp(question=question, session_id=session_id):
                if event["type"] == "meta":
                    yield _sse_event("meta", {
                        "code": event.get("code", 200),
                        "msg": event.get("msg"),
                        "session_id": event["session_id"],
                    })
                elif event["type"] == "token":
                    yield _sse_event("token", {"token": event["token"]})
                elif event["type"] == "done":
                    yield _sse_event("done", {"sources": event.get("sources", [])})
        except Exception as e:
            logger.error(f"tempAsk 流式异常: {e}")
            yield _sse_event("done", {"sources": [], "error": str(e)})

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "X-Accel-Buffering": "no",  # 关掉 Nginx 缓冲
        },
    )


# =================================================================
# 2. 历史会话发消息 — SSE 流式（需登录）
# =================================================================

@router.post("/sendMessage")
async def send_message(
    request: Request,
    question: str = Query(..., min_length=1, max_length=2000, description="用户问题"),
    session_id: Optional[int] = Query(None, description="会话ID，续聊时传入"),
    db: AsyncSession = Depends(get_database),
):
    """
    历史会话 AI 问答（SSE 流式响应，需登录）。

    有 session_id 则续聊，无则自动新建会话。
    消息持久化到 MySQL，缓存到 Redis。
    """

    user_id = _require_login(request)
    if user_id is None:
        # 未登录返回 SSE 错误
        async def error_gen():
            yield _sse_event("meta", {"code": 401, "msg": "请先登录", "session_id": None})
            yield _sse_event("done", {"sources": []})
        return StreamingResponse(error_gen(), media_type="text/event-stream",
                                 headers={"Cache-Control": "no-cache"})

    redis = get_redis()

    async def event_generator():
        try:
            async for event in chat_service.ask_session(
                session_id=session_id,
                question=question,
                user_id=user_id,
                db=db,
                redis=redis,
            ):
                if event["type"] == "meta":
                    yield _sse_event("meta", {
                        "code": event.get("code", 200),
                        "msg": event.get("msg"),
                        "session_id": event["session_id"],
                    })
                elif event["type"] == "token":
                    yield _sse_event("token", {"token": event["token"]})
                elif event["type"] == "done":
                    yield _sse_event("done", {"sources": event.get("sources", [])})
        except Exception as e:
            logger.error(f"sendMessage 流式异常: {e}")
            yield _sse_event("done", {"sources": [], "error": str(e)})

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "X-Accel-Buffering": "no",
        },
    )


# =================================================================
# 3. 查询会话列表 — JSON（需登录）
# =================================================================

@router.post("/searchSession")
async def search_session(
    request: Request,
    db: AsyncSession = Depends(get_database),
):
    """
    查询当前登录用户的所有历史会话摘要列表（JSON 响应）。

    请求体: {}（可为空）
    响应: Result { code, msg, data: { sessions, total } }
    """
    user_id = _require_login(request)
    if user_id is None:
        return Result(code=401, msg="请先登录", data=None)

    try:
        sessions = await chat_service.list_sessions(db=db, user_id=user_id)
        return Result(code=200, msg=None, data={
            "sessions": sessions,
            "total": len(sessions),
        })
    except Exception as e:
        logger.error(f"searchSession 异常: {e}")
        return Result(code=500, msg=str(e), data=None)


# =================================================================
# 4. 查询会话详情 — JSON（需登录）
# =================================================================

@router.post("/getSession")
async def get_session(
    request: Request,
    db: AsyncSession = Depends(get_database),
):
    """
    查询单条历史会话的完整消息内容（JSON 响应）。

    请求体: {"session_id": 8732390091937312768}
    响应: Result { code, msg, data: { session, messages } }
    """
    user_id = _require_login(request)
    if user_id is None:
        return Result(code=401, msg="请先登录", data=None)

    # 从请求体解析 session_id
    body = {}
    try:
        body_bytes = await request.body()
        if body_bytes:
            body = json.loads(body_bytes.decode())
    except Exception:
        pass

    session_id = body.get("session_id")
    if not session_id:
        return Result(code=201, msg="缺少 session_id 参数", data=None)

    try:
        detail = await chat_service.get_session_detail(
            db=db, session_id=int(session_id), user_id=user_id
        )
        if detail is None:
            return Result(code=201, msg="会话不存在或无权访问", data=None)
        return Result(code=200, msg=None, data=detail)
    except Exception as e:
        logger.error(f"getSession 异常: {e}")
        return Result(code=500, msg=str(e), data=None)


# =================================================================
# 5. 删除会话 — JSON（需登录）
# =================================================================

@router.post("/removeSession")
async def remove_session(
    request: Request,
    db: AsyncSession = Depends(get_database),
):
    """
    物理删除一条历史会话及其所有消息（JSON 响应）。

    请求体: {"session_id": 8732390091937312768}
    响应: Result { code, msg, data }
    """
    user_id = _require_login(request)
    if user_id is None:
        return Result(code=401, msg="请先登录", data=None)

    body = {}
    try:
        body_bytes = await request.body()
        if body_bytes:
            body = json.loads(body_bytes.decode())
    except Exception:
        pass

    session_id = body.get("session_id")
    if not session_id:
        return Result(code=201, msg="缺少 session_id 参数", data=None)

    redis = get_redis()
    try:
        success = await chat_service.delete_session(
            db=db, redis=redis, session_id=int(session_id), user_id=user_id
        )
        if not success:
            return Result(code=201, msg="会话不存在或无权删除", data=None)
        return Result(code=200, msg="删除成功", data=None)
    except Exception as e:
        logger.error(f"removeSession 异常: {e}")
        return Result(code=500, msg=str(e), data=None)
