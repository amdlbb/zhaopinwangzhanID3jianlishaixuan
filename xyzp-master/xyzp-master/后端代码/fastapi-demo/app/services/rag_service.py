#TODO:rag_service的详细开发，在这里最终负责chain链条的最终组装
"""
rag_service.py — RAG 检索 + LLM 生成（LangChain chain）
=========================================================

【功能】
  ChromaDB 向量检索 → LangChain prompt → DeepSeek 生成。
  提供两个方法给 chat_service.py：同步 ask() 和流式 ask_stream()。
  提示词模板（PromptTemplate）定义在此文件内。

【依赖】
  - ChromaDB（只读，集合 yuncai_rag）
  - BAAI/bge-small-zh-v1.5
  - LangChain: Chroma retriever + create_stuff_documents_chain + ChatOpenAI
  - DeepSeek API（通过 OpenAI SDK 兼容层调用）

【关键方法】
  ask(question: str)
      → {"answer": str, "sources": list[dict]}
      — 同步方法，等 LLM 全部跑完再返回完整结果。
        用于 searchSession/getSession 等不需要流式的场景的上下文拼接。

  ask_stream(question: str) — AsyncGenerator[dict]
      → yield {"type": "token", "token": "***"}
      → yield {"type": "token", "token": "***"}
      → ...
      → yield {"type": "done", "answer": "完整回答", "sources": [...]}
      — 异步生成器，通过 chain.astream() 逐 token 产出。
        由 chat_service 订阅，再包装成 SSE 事件格式。
        ★ 不负责 SSE 格式编码，只产出结构化 dict。
"""













