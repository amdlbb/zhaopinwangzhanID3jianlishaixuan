"""
interceptor - 请求拦截器 / 中间件包

提供认证中间件，通过 Feign 调用 Java webservice 校验登录状态，
将 user_id 注入 request.state 供后续路由使用。
"""

from .interceptor import auth_middleware

__all__ = ["auth_middleware"]
