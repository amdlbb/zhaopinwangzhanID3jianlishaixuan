#TODO:聊天 API 路由层

"""
chatrouter.py — 聊天 API 路由层
================================

【功能】
  遵循 Java controller 命名风格，只用 POST，行为写进路径名。

【接口】
  POST /chat/tempAsk          — 临时会话（免登录，InMemoryChatHistory）
                                 ⚡ 响应格式: SSE (text/event-stream)
                                 事件: meta → token × N → done
  POST /chat/sendMessage      — 历史会话发消息（需登录，有 session_id 续聊，无则新建）
                                 ⚡ 响应格式: SSE (text/event-stream)
                                 事件: meta → token × N → done
  POST /chat/searchSession    — 查询用户历史会话列表（需登录）
                                 响应格式: JSON (Result)
  POST /chat/getSession       — 查询单条会话详情（需登录，传入 session_id）
                                 响应格式: JSON (Result)
  POST /chat/removeSession    — 删除会话（需登录，物理删除）
                                 响应格式: JSON (Result)

【SSE 事件定义】
  event: meta
  data: {"code": 200, "msg": null, "session_id": "..."}
  — 连接建立后第一时间发送，告知元数据

  event: token
  data: {"token": "..."}
  — 每从 LLM 收到一个 token 立即推送给前端

  event: done
  data: {"sources": [...]}
  — 流结束信号，附带 RAG 来源信息，前端收到后关闭 EventSource

【SSE 实现说明】
  1. 路由处理函数返回 StreamingResponse 而非 Result
  2. 需设置 Content-Type: text/event-stream
  3. 需关掉 Gateway/Nginx 的响应体缓冲
"""

from fastapi import APIRouter

router = APIRouter()
