"""
interceptor.py - 认证中间件
=============================

【功能】
  拦截前端请求，从请求头提取 token，通过 Feign 调用 Java webservice
  校验登录状态，将 user_id 注入 request.state 中供后续路由使用。

【流程】
  1. 从请求头提取 token
  2. 如果有 token，调用 AuthorizationFeignClient.check_login()
  3. 将 is_logged_in / user_id 写入 request.state
  4. 无论是否登录都放行（放行后再由具体路由决定是否需要登录）

【典型响应示例】
  {
    "code": 200,
    "msg": "null",
    "data": {
      "isLoggedIn": true,
      "userId": "123456789"
    }
  }
"""

import logging
from fastapi import Request

from app.core.feginclient import AuthorizationFeignClient

logger = logging.getLogger(__name__)

# 全局单例（复用 Feign 实例，避免每次请求都创建）
_auth_client = AuthorizationFeignClient()


def _set_unauthenticated(request: Request) -> None:
    """将请求标记为未登录状态"""
    request.state.is_logged_in = False
    request.state.user_id = None


async def auth_middleware(request: Request, call_next):
    """
    认证中间件 - 在每个 HTTP 请求前执行。

    从请求头提取 token → 调用 Java 服务校验 → 注入 request.state → 放行。

    Args:
        request:   FastAPI 请求对象
        call_next: 下一个中间件或路由处理器

    Returns:
        HTTP 响应
    """
    # 1. 提取 token
    token = request.headers.get("token", "")

    if token and token.strip():
        # 2. 有 token → 调用 Java 服务校验
        try:
            result = await _auth_client.check_login(headers={"token": token.strip()})
            if result.get("code") == 200 or result.get("code") == "200":
                data = result.get("data", {}) or {}
                request.state.is_logged_in = data.get("isLoggedIn", False)
                request.state.user_id = data.get("userId")
            else:
                logger.warning(f"Feign 认证返回异常: {result}")
                _set_unauthenticated(request)
        except Exception as e:
            logger.error(f"Feign 调用认证接口失败: {e}")
            _set_unauthenticated(request)
    else:
        # 3. 无 token → 直接标记未登录
        _set_unauthenticated(request)

    # 4. 放行（无论是否登录，请求都会继续）
    response = await call_next(request)
    return response
