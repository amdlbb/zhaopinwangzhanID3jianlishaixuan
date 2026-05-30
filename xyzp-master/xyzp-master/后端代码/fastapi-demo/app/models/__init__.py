"""
models 包 - 数据库 ORM 模型层
=================================

存放 SQLAlchemy ORM 模型，每个文件对应一张或多张表。

导入所有模型以确保 SQLAlchemy 能扫描到它们（用于建表）：
  from app.models.base import Base
  from app.models.user import User       # 导入后 User 会被注册到 Base.metadata
"""

from app.models.base import Base
from app.models.user import User

# 把所有模型导出，方便 metadata.create_all() 扫描
__all__ = ["Base", "User"]
