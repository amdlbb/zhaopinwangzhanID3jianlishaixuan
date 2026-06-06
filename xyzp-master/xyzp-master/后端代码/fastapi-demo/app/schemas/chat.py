#TODO:以下会话类仅供参考实际请以文档为准，如业务需要可自定义
"""
chat.py — 聊天数据模型 + LangChain 提示词模板
================================================

【功能】
  1. Pydantic 模型：定义所有聊天接口的请求/响应数据结构
  2. ChatPromptTemplate：统一定义提示词模板和消息格式
  3. 临时对话和历史对话使用不同的模板结构

【设计说明】
  - 提示词模板看作"prompt 的 schema"——定义对话结构的契约
  - 临时会话：system + 输入（无历史，不存库）
  - 历史会话：system + MessagesPlaceholder("history") + 输入
"""

from typing import Optional
from pydantic import BaseModel, Field

from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder


# =====================================================================
# 请求体
# =====================================================================

class AskRequest(BaseModel):
    """聊天请求体（临时会话 & 历史会话通用）"""
    question: str = Field(..., min_length=1, max_length=2000)


# =====================================================================
# 响应体
# =====================================================================

class SourceItem(BaseModel):
    """RAG 检索来源"""
    title: str = ""
    score: float = 0.0
    snippet: str = ""


class AskResponse(BaseModel):
    """临时会话响应"""
    answer: str
    sources: list[SourceItem] = []


class MessageItem(BaseModel):
    """单条消息"""
    id: int = 0
    role: str                                # user / assistant
    content: str
    sources: list[SourceItem] = []
    created_at: Optional[str] = None


class SessionSummary(BaseModel):
    """会话摘要（列表用）"""
    id: int
    title: Optional[str] = None
    message_count: int = 0
    created_at: Optional[str] = None
    updated_at: Optional[str] = None


class SessionListResponse(BaseModel):
    """历史会话列表"""
    sessions: list[SessionSummary] = []
    total: int = 0


class SessionDetailResponse(BaseModel):
    """会话详情"""
    session: SessionSummary
    messages: list[MessageItem] = []


class SessionMessageResponse(BaseModel):
    """发消息后响应"""
    session_id: int
    answer: str
    sources: list[SourceItem] = []


# =====================================================================
# LangChain 提示词模板
# =====================================================================

SESSION_CHAT_PROMPT = ChatPromptTemplate.from_messages([
    (
        "system",
        "你是一个专业的AI客服助手「云才科技」。"
        "根据提供的参考信息，用中文回答用户关于公司、招聘、投递、面试等问题。"
        "如果参考信息不足以回答，如实说不知道，不要编造。",
    ),
    MessagesPlaceholder(variable_name="history"),
    (
        "human",
        "用户问题: {input}\n\n"
        "参考信息:\n{context}",
    ),
])
