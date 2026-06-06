"""
redis.py - Redis 客户端配置
==============================

【功能】
  提供异步 Redis 连接获取函数，供服务层和路由层使用。
"""

import redis.asyncio as aioredis
from app.config import settings

# 全局 Redis 连接池（异步）
_pool: aioredis.ConnectionPool | None = None


def _get_pool() -> aioredis.ConnectionPool:
    global _pool
    if _pool is None:
        _pool = aioredis.ConnectionPool(
            host=settings.REDIS_URL,
            port=settings.REDIS_PORT,
            db=settings.REDIS_DB,
            decode_responses=True,
            max_connections=20,
        )
    return _pool


def get_redis() -> aioredis.Redis:
    """获取异步 Redis 客户端实例"""
    return aioredis.Redis(connection_pool=_get_pool())
