"""
user.py - 用户 ORM 模型
========================

【功能】
  用户表（users）的 ORM 映射。一个 Python 类对应一张数据库表。
  定义字段名、类型、约束等。

【类比 Java】
  @Entity
  @Table(name = "users")
  public class User {
      @Id @GeneratedValue private Long id;
      @Column(nullable = false) private String username;
      ...
  }

【用法】
  新增用户： user = User(username="螯", ...)
            session.add(user)
  查询用户： await session.execute(select(User).where(...))
"""

from sqlalchemy import Column, Integer, String, Boolean, DateTime, func
from app.models.base import Base


class User(Base):
    """
    用户表
    对应数据库中的 users 表
    """
    __tablename__ = "users"  # 表名

    # ===== 字段定义 =====
    id = Column(Integer, primary_key=True, autoincrement=True, comment="用户ID")
    username = Column(String(50), nullable=False, unique=True, comment="用户名")
    password = Column(String(255), nullable=False, comment="密码（加密后）")
    email = Column(String(100), nullable=True, comment="邮箱")
    is_active = Column(Boolean, default=True, comment="是否启用")
    created_at = Column(DateTime, server_default=func.now(), comment="创建时间")
    updated_at = Column(DateTime, server_default=func.now(), onupdate=func.now(), comment="更新时间")

    def __repr__(self):
        """打印友好（调试用）"""
        return f"<User(id={self.id}, username='{self.username}', is_active={self.is_active})>"
