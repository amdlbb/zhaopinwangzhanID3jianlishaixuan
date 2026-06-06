"""
rag_service.py — RAG 检索 + LLM 生成（LangChain chain）
=========================================================

【功能】
  ChromaDB 向量检索 → LangChain prompt → DeepSeek 生成。
  提供两个方法给 chat_service.py：同步 ask() 和流式 ask_stream()。

【依赖】
  - ChromaDB（只读，集合 yuncai_rag）
  - BAAI/bge-small-zh-v1.5
  - LangChain: Chroma retriever + ChatPromptTemplate + ChatDeepSeek
  - DeepSeek API（通过 LangChain ChatDeepSeek 调用）

【关键方法】
  ask(question: str, chat_history=None)
      → {"answer": str, "sources": list[dict]}
      — 同步方法，等 LLM 全部跑完再返回完整结果。

  ask_stream(question: str, chat_history=None)
      — AsyncGenerator，逐 token 产出结构化 dict。
"""

import json
import logging
from typing import AsyncGenerator, Optional, List

from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.messages import BaseMessage
from langchain_deepseek import ChatDeepSeek

from app.config import settings
from app.core.vector_store import VectorStore
from app.schemas.chat import SESSION_CHAT_PROMPT, TEMP_CHAT_PROMPT

logger = logging.getLogger(__name__)

# 全局单例（模型加载重，避免每次请求都初始化）
_vector_store: Optional[VectorStore] = None
_llm: Optional[ChatDeepSeek] = None


def _get_vector_store() -> VectorStore:
    global _vector_store
    if _vector_store is None:
        _vector_store = VectorStore()
    return _vector_store


def _get_llm() -> ChatDeepSeek:
    global _llm
    if _llm is None:
        _llm = ChatDeepSeek(
            model=settings.AI_MODEL,
            api_key=settings.API_KEY,
            api_base=settings.BASE_URL,
            temperature=0.7,
            streaming=True,
        )
    return _llm


def _build_prompt(has_history: bool = False) -> ChatPromptTemplate:
    """构建提示词模板：有历史记录使用 SESSION 模板，否则用 TEMP 模板"""
    if has_history:
        return SESSION_CHAT_PROMPT
    return TEMP_CHAT_PROMPT


def _format_sources(docs_with_scores: list) -> list[dict]:
    """将检索结果格式化为 API 返回值"""
    sources = []
    for doc, score in docs_with_scores:
        snippet = doc.page_content[:200].replace("\n", " ").strip()
        title = doc.metadata.get("source", "未知文档")
        sources.append({
            "title": title,
            "score": round(score, 4),
            "snippet": snippet,
        })
    return sources


async def ask(
    question: str,
    chat_history: Optional[List[BaseMessage]] = None,
) -> dict:
    """
    同步 RAG 问答：检索 + 生成，等待完整结果后返回。

    Args:
        question: 用户问题
        chat_history: 历史消息列表（None 表示新对话）

    Returns:
        {"answer": str, "sources": [{"title": "", "score": 0.0, "snippet": ""}]}
    """
    vs = _get_vector_store()
    llm = _get_llm()

    # 1. 向量检索
    k = settings.SIMILARITY
    docs_with_scores = vs.similarity_search(question, k=k)

    # 2. 拼接上下文
    context = "\n\n".join(doc.page_content for doc, _ in docs_with_scores)

    # 3. 构建 prompt
    prompt = _build_prompt(has_history=chat_history is not None and len(chat_history) > 0)

    # 4. 构建消息
    messages = {}
    if chat_history:
        messages["history"] = chat_history
    messages["input"] = question
    messages["context"] = context

    # 5. 调用 LLM
    chain = prompt | llm
    response = await chain.ainvoke(messages)

    answer = response.content if hasattr(response, "content") else str(response)

    return {
        "answer": answer,
        "sources": _format_sources(docs_with_scores),
    }


async def ask_stream(
    question: str,
    chat_history: Optional[List[BaseMessage]] = None,
) -> AsyncGenerator[dict, None]:
    """
    流式 RAG 问答：逐 token 产出，供 SSE 推送。

    Yields:
        {"type": "token", "token": "***"}
        ...
        {"type": "done", "answer": "完整回答", "sources": [...]}

    Args:
        question: 用户问题
        chat_history: 历史消息列表
    """
    vs = _get_vector_store()
    llm = _get_llm()

    # 1. 向量检索
    k = settings.SIMILARITY
    docs_with_scores = vs.similarity_search(question, k=k)

    # 2. 拼接上下文
    context = "\n\n".join(doc.page_content for doc, _ in docs_with_scores)

    # 3. 构建 prompt
    prompt = _build_prompt(has_history=chat_history is not None and len(chat_history) > 0)

    # 4. 构建消息
    messages = {}
    if chat_history:
        messages["history"] = chat_history
    messages["input"] = question
    messages["context"] = context

    # 5. 流式调用 LLM
    chain = prompt | llm
    full_answer = ""
    try:
        async for chunk in chain.astream(messages):
            token = chunk.content if hasattr(chunk, "content") and chunk.content else ""
            if token:
                full_answer += token
                yield {"type": "token", "token": token}
    except Exception as e:
        logger.error(f"LLM 流式生成失败: {e}")
        yield {"type": "token", "token": f"[生成错误: {e}]"}

    # 6. 流结束，返回完整结果
    sources = _format_sources(docs_with_scores)
    yield {
        "type": "done",
        "answer": full_answer,
        "sources": sources,
    }
