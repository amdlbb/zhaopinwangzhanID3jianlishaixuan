"""
main.py - FastAPI 应用入口
==============================

【功能】
  - 创建 FastAPI 应用实例
  - 注册所有路由
  - 注册生命周期钩子（启动时建表、关闭时清理）
  - 挂载根路径的健康检查、Hello World 等基础接口

【类比 Java】
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  }

【启动】
  uvicorn app.main:app --reload --host 0.0.0.0 --port 8000

  --reload 表示热重载（改代码自动重启，开发时用）
  注意：必须在项目根目录（fastapi-demo/）下执行
"""

from contextlib import asynccontextmanager
from fastapi import FastAPI, HTTPException, Path
from typing import Any, AsyncGenerator

from fastapi.exceptions import RequestValidationError

from app.config import settings
from app.core.database import engine
from app.core.nacos_client import NacosService
from app.models import Base          # 导入所有 ORM 模型
from app.routers import items_router, user_router
from app.schemas.common import Result
from app.utils.exception import general_exception_handler, http_exception_handler, validation_exception_handler


# ============================================================
# 生命周期管理（类似 Spring 的 @PostConstruct / @PreDestroy）
# ============================================================
@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    应用启动/关闭时的钩子

    startup:  自动创建所有未存在的数据库表（开发方便）
    shutdown: 关闭数据库引擎（释放连接池）

    【类比 Java】
    @EventListener(ApplicationReadyEvent.class)
    public void onStart() { ... }

    @PreDestroy
    public void onShutdown() { ... }
    """
    # -------- 启动时执行 ----------
    print("🚀 应用启动中...")
    async with engine.begin() as conn:
        # 自动创建所有继承 Base 的模型对应的表
        # 如果表已存在则跳过，不会覆盖
        await conn.run_sync(Base.metadata.create_all)
    print("✅ 数据库表初始化完成")

    yield  # 应用运行期间挂在这里

    # -------- 关闭时执行 ----------
    print("🛑 应用关闭中...")
    await engine.dispose()  # 关闭连接池
    print("✅ 连接池已释放")

    # --- 启动阶段 (替代原来的 @app.on_event("startup")) ---
    # 1. 服务注册
    nacos_service.register("127.0.0.1", 8000)
    # 2. 获取配置并挂载到 app.state 上，方便在API中访问
    config = nacos_service.get_config("fastapi-config.yaml")
    if config:
        app.state.config = config

    # --- 关键点：yield 将应用的控制权交还给 FastAPI ---
    yield

    # --- 关闭阶段 (替代原来的 @app.on_event("shutdown")) ---
    # 应用关闭前，从Nacos注销服务
    nacos_service.deregister("127.0.0.1", 8000)


# ============================================================
# 创建 FastAPI 应用实例
# ============================================================
app = FastAPI(
    title=settings.APP_TITLE,
    version=settings.APP_VERSION,
    description=settings.APP_DESCRIPTION,
    lifespan=lifespan,
)

# ============================================================
# 注册异常处理类
# ============================================================
app.add_exception_handler(HTTPException, http_exception_handler)
app.add_exception_handler(RequestValidationError, validation_exception_handler)
app.add_exception_handler(Exception, general_exception_handler)

# ============================================================
# 注册路由
# ============================================================
app.include_router(items_router, prefix="/items", tags=["商品管理"])
app.include_router(user_router)  # prefix 已在 user.py 中定义
nacos_service = NacosService()


# ============================================================
# 基础 Hello World 接口
# ============================================================
@app.get("/", tags=["基础"])
async def root():
    """
    根路径 - Hello World
    GET /
    """
    return {"message": "Hello，World!"}



@app.get("/hello/{word}", tags=["基础"])
async def say_hello(word: str = Path(..., min_length=2, description="你的名字")):
    """
    向指定对象打招呼
    GET /hello/螯

    【对比 Java】
    @GetMapping("/hello/{word}")
    public Map<String, String> sayHello(@PathVariable @Size(min=2) String word)
    """
    return {"message": f"hello, {word}"}


@app.get("/health", tags=["基础"])
async def health_check():
    """
    健康检查
    GET /health
    用于负载均衡/监控系统检查服务是否存活
    """
    return {
        "status": "ok",
        "version": settings.APP_VERSION,
    }


@app.get("/result/{msg}", tags=["基础"])
async def get_result(msg: str):
    """
    演示统一响应格式
    GET /result/hello
    """
    return Result(code=200, message=f"{msg}", data=[])


if __name__ == "__main__":
    """直接运行此文件时启动 uvicorn（类似 java -jar）"""
    import uvicorn
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=True,
    )
