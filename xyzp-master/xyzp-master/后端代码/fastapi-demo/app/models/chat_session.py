"""
chat_session.py - 会话 ORM 模型
==================================

【功能】
  聊天会话表（kfzs.chat_sessions）的 ORM 映射。
  每创建一个历史会话就插入一条记录。

【类比 Java】
  @Entity
  @Table(name = "chat_sessions")
  public class ChatSession {
      @Id private Long id;
      private Long userId;
      private String title;
      private Integer status;
  }

【字段说明】
  id         : 会话ID，用雪花算法/时间戳生成（非自增）
  user_id    : 用户ID，来自 Java JWT 解析结果
  title      : 从用户第一条消息自动截取（前 30 字 + "..."）
  status     : 1=进行中  0=已删除（软删除）
  created_at : 创建时间
  updated_at : 最后更新时间
"""

from sqlalchemy import Column, String, BigInteger, DateTime, Integer, Text, func
from app.models.base import Base


class ChatSession(Base):
    """聊天会话表"""
    __tablename__ = "chat_sessions"
    __table_args__ = {"comment": "聊天会话表"}

    id = Column(
        BigInteger,
        primary_key=True,
        autoincrement=False,
        comment="会话ID（时间戳ID）",
    )
    user_id = Column(
        BigInteger,
        nullable=True,
        index=True,
        comment="用户ID（来自Java JWT），临时会话留空",
    )
    title = Column(
        String(100),
        nullable=True,
        comment="会话标题（自动取首条消息前30字）",
    )
    status = Column(
        Integer,
        default=1,
        comment="状态: 1=进行中, 0=已删除",
    )
    created_at = Column(
        DateTime,
        server_default=func.now(),
        nullable=False,
        comment="创建时间",
    )
    updated_at = Column(
        DateTime,
        server_default=func.now(),
        onupdate=func.now(),
        nullable=False,
        comment="最后更新时间",
    )

    def __repr__(self):
        return (
            f"<ChatSession(id={self.id}, user_id={self.user_id}, "
            f"title='{self.title}', status={self.status})>"
        )
