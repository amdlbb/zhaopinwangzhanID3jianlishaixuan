"""
routers 包 - API 路由层
=========================

只做导入转发，方便 main.py 中统一引入：

  from app.routers import items_router, user_router

而不是去每个文件里找 router 对象。
"""

from .items import router as items_router
from .user import router as user_router

__all__ = ["items_router", "user_router"]
