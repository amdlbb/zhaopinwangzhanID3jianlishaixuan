"""
chat_message.py - 消息 ORM 模型
==================================

【功能】
  聊天消息表（kfzs.chat_messages）的 ORM 映射。
  每条用户/AI 消息存一条记录，外键关联 chat_sessions。

【类比 Java】
  @Entity
  @Table(name = "chat_messages")
  public class ChatMessage {
      @Id @GeneratedValue private Long id;
      private Long sessionId;
      private String role;
      private String content;
      private String sources;
  }

【字段说明】
  id         : 自增主键（消息量大，用自增更简单）
  session_id : 外键 → chat_sessions.id
  role       : "user" 或 "assistant"
  content    : 消息内容（文本）
  sources    : RAG 检索来源，JSON 数组字符串 ["文件名", ...]
  created_at : 发送时间

【存储说明】
  - 历史消息全量存 MySQL
  - 最近 N 条同步到 Redis hash（见 chat_service.py）
"""

from sqlalchemy import Column, BigInteger, DateTime, Integer, Text, String, func
from app.models.base import Base


class ChatMessage(Base):
    """聊天消息表"""
    __tablename__ = "chat_messages"
    __table_args__ = {"comment": "聊天消息表"}

    id = Column(
        BigInteger,
        primary_key=True,
        autoincrement=True,
        comment="消息ID（自增）",
    )
    session_id = Column(
        BigInteger,
        nullable=False,
        index=True,
        comment="会话ID → chat_sessions.id",
    )
    role = Column(
        String(20),
        nullable=False,
        comment="角色: user / assistant",
    )
    content = Column(
        Text,
        nullable=False,
        comment="消息内容",
    )
    sources = Column(
        Text,
        nullable=True,
        comment="RAG来源（JSON数组，如 [\"文档1\", \"文档2\"]）",
    )
    created_at = Column(
        "created_time",
        DateTime,
        server_default=func.now(),
        nullable=False,
        comment="发送时间",
    )

    def __repr__(self):
        preview = self.content[:30] if self.content else ""
        return (
            f"<ChatMessage(id={self.id}, session_id={self.session_id}, "
            f"role='{self.role}', content='{preview}...')>"
        )
