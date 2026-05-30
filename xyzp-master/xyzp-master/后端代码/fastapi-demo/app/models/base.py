"""
base.py - ORM 声明基类
========================

【功能】
  SQLAlchemy 的 ORM 模型声明基类。所有数据库模型都继承它。
  类似于 JPA 的 @MappedSuperclass 或 Hibernate 的 BaseEntity。

【类比 Java】
  @MappedSuperclass
  public class BaseEntity {
      @Id @GeneratedValue private Long id;
  }

【用法】
  from app.models.base import Base
  class User(Base):
      __tablename__ = "users"
      ...
"""

from sqlalchemy.orm import DeclarativeBase


class Base(DeclarativeBase):
    """
    SQLAlchemy 2.0 的声明式基类。
    所有 ORM 模型继承这个类，SQLAlchemy 会自动扫描并创建对应的表。
    """
    pass
