"""
feginclient.py - 基于 Nacos 服务发现的 Feign 客户端
====================================================

【功能】
  通过 Nacos 发现 Java 后端服务（webservice），发起 HTTP 调用。
  提供 FeignClient + GetMapping 装饰器，与用户给出的 Python 代码风格一致。

【用法】
  client = AuthorizationFeignClient()
  result = await client.check_login(headers={"token": "xxx"})

【依赖】
  - nacos sdk（已装）
  - httpx（已装）
"""

import logging
from typing import Optional

import httpx
from nacos import NacosClient

logger = logging.getLogger(__name__)

# Nacos 服务器地址（与 core/nacos_client.py 保持一致）
NACOS_SERVER = "192.168.44.1:8848"
NAMESPACE = "public"


# =====================================================================
# FeignClient — 核心客户端
# =====================================================================

class FeignClient:
    """
    基于 Nacos 服务发现的 HTTP 客户端。

    通过服务名从 Nacos 获取实例地址，发起请求。
    类似于 Spring Cloud OpenFeign 的 @FeignClient(name="service-name")。

    用法:
        feign = FeignClient("webservice")
        result = await feign._request("GET", "/api/path", headers={...})
    """

    def __init__(self, service_name: str):
        self.service_name = service_name
        self._nacos = NacosClient(NACOS_SERVER, namespace=NAMESPACE)

    def _get_instance(self) -> Optional[dict]:
        """
        从 Nacos 获取一个健康实例。
        返回: {"ip": "192.168.44.1", "port": 8081, ...} 或 None
        """
        try:
            resp = self._nacos.list_naming_instance(self.service_name)
            hosts = resp.get("hosts", [])
            # 优先选健康实例
            for h in hosts:
                if h.get("healthy"):
                    return h
            return hosts[0] if hosts else None
        except Exception as e:
            logger.error(f"Nacos 服务发现失败 [{self.service_name}]: {e}")
            return None

    async def _request(self, method: str, path: str, headers: Optional[dict] = None) -> dict:
        """
        发送 HTTP 请求。

        Args:
            method: GET / POST 等
            path:   API 路径，如 /api/authorization/check-login
            headers: 请求头，如 {"token": "xxx"}

        Returns:
            解析后的 JSON 响应字典
        """
        instance = self._get_instance()
        if not instance:
            logger.warning(f"服务 [{self.service_name}] 无可用实例")
            return {"code": 500, "msg": f"服务 [{self.service_name}] 不可用", "data": None}

        url = f"http://{instance['ip']}:{instance['port']}{path}"

        async with httpx.AsyncClient(timeout=10.0) as client:
            try:
                resp = await client.request(method, url, headers=headers or {})
                return resp.json()
            except Exception as e:
                logger.error(f"Feign 调用失败 [{method} {url}]: {e}")
                return {"code": 500, "msg": str(e), "data": None}


# =====================================================================
# GetMapping 装饰器
# =====================================================================

class GetMapping:
    """
    GET 请求映射装饰器。

    将类方法标记为 GET 请求，自动从 self.feign 获取 FeignClient 实例并发起调用。

    用法:
        class MyClient:
            def __init__(self):
                self.feign = FeignClient("webservice")

            @GetMapping("/api/authorization/check-login")
            async def check_login(self, headers: dict) -> dict:
                \"""
                方法体不需要写任何代码，装饰器会自动完成调用。
                但为了代码可读性和接口文档生成，保留 pass 即可。
                \"""
                ...
    """

    def __init__(self, path: str):
        self.path = path

    def __call__(self, func):
        import functools

        @functools.wraps(func)
        async def wrapper(instance, *args, **kwargs):
            # 从实例中获取 feign 属性
            feign: FeignClient = getattr(instance, "feign", None)
            if feign is None:
                raise AttributeError(
                    f"{type(instance).__name__} 缺少 feign 属性，"
                    f"请在 __init__ 中初始化: self.feign = FeignClient('service-name')"
                )
            # 提取 headers 参数
            headers = kwargs.get("headers", args[0] if args else {})
            # 发起 GET 请求
            return await feign._request("GET", self.path, headers=headers)

        return wrapper


# =====================================================================
# AuthorizationFeignClient — Java webservice 认证接口
# =====================================================================

class AuthorizationFeignClient:
    """
    Java webservice 服务的认证接口 Feign 客户端。

    目标服务注册在 Nacos 中，服务名为 "webservice"。
    调用接口: GET /api/authorization/check-login
    接口响应:
        {
            "code": 200,
            "msg": "null",
            "data": {
                "isLoggedIn": true,
                "userId": "123456789"
            }
        }

    ⚠️ 注意: Java 后端 AuthorizationController.checkLogin 认 token 请求头。
      Python 端接收前端 token 后直接透传，不做任何转换。
    """

    def __init__(self):
        self.feign = FeignClient(service_name="webservice")

    async def check_login(self, headers: dict) -> dict:
        """
        检查登录状态。

        Args:
            headers: 请求头，应包含 token: "xxx"

        Returns:
            {"code": 200, "msg": "...", "data": {"isLoggedIn": bool, "userId": str}}
        """
        return await self.feign._request(
            "GET",
            "/api/authorization/check-login",
            headers=headers or {},
        )
