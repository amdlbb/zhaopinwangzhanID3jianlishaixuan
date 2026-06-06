#TODO:chat_service.py的详细开发
"""
chat_service.py — 聊天业务编排层
===================================

【功能】
  会话/消息的 CRUD 管理 + Redis 缓存调度 + 对接 rag_service。
  继承 LangChain BaseChatMessageHistory 的 ChatHistoryStore 在此文件内实例化使用。

【流式设计】
  ask_temp() 和 ask_session() 改为异步生成器（AsyncGenerator），
  产出结构化 dict 事件，由路由层（chatrouter.py）包装为 SSE wire format。

  事件时序：
    ask_temp:
      yield {"type": "meta", "session_id": "..."}     ← 连接建立，告知 session_id
      yield {"type": "token", "token": "***"}       ← 每收到一个 LLM token
      yield {"type": "token", "token": "***"}       ← ...
      yield {"type": "done", "answer": "...", "sources": [...]}  ← 流结束

    ask_session:
      ① 校验 session_id，不存在则新建 → 写用户消息到 MySQL
      yield {"type": "meta", "session_id": int}       ← 告知 session_id
      ② 调 rag_service.ask_stream()，逐 token 转发，同时缓冲到 list
      yield {"type": "token", "token": "***"}
      yield {"type": "token", "token": "***"}
      ...
      ③ 流结束，将缓冲的完整 answer 写入 MySQL + Redis
      yield {"type": "done", "answer": "完整回答", "sources": [...]}

【关键方法】
  ask_temp(question: str)              — AsyncGenerator[dict]
      → yield meta → token × N → done
      临时会话，数据不落库，直接调 rag_service.ask_stream()。

  create_session(user_id: int)         → ChatSession
      建会话行。不公开调用，由 ask_session 在首次发消息时自动创建。

  ask_session(session_id, question,
              user_id)                 — AsyncGenerator[dict]
      → yield meta → token × N → done
      历史会话。先存用户消息 → 再流式生成 → 流结束后存完整 AI 回答。
      ★ DB 写入仅发生在 meta 之前（用户消息）和 done 之后（AI 回答）。
         流式期间不做任何 I/O。

  list_sessions(user_id)               → list[SessionSummary]
  get_session(session_id, user_id)     → SessionDetailResponse
  delete_session(session_id, user_id)  → None
      以上三个接口不需要流式，保持同步 JSON 返回。
"""







