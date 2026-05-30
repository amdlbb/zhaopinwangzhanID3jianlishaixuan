"""
database.py - 数据库核心配置
=================================

【功能】
  集中管理数据库引擎和会话工厂的创建。
  其他模块（services / routers）通过 get_database() 依赖注入获取 session。

【类比 Java】
  @Configuration + @Bean 方法
  或者 MyBatis 的 SqlSessionFactory 配置类

【用法】
  from app.core.database import get_database
  async with get_database() as session:
      session.execute(...)
"""

from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker, AsyncSession

# ===== 数据库连接 URL =====
# 格式: mysql+aiomysql://用户名:密码@主机:端口/库名?参数
URL = "mysql+aiomysql://root:123456@localhost:3306/kfzs"

# ===== 创建异步引擎 =====
# 引擎是 SQLAlchemy 的核心，负责管理数据库连接池
engine = create_async_engine(
    URL,
    echo=True,               # 打印 SQL 语句（调试用，生产环境关掉）
    pool_size=10,            # 连接池大小
    max_overflow=20,         # 超过 pool_size 后最多再创建多少连接
)

# ===== 创建会话工厂 =====
# sessionmaker 是"会话工厂"，每次调用 () 都返回一个新的 AsyncSession
# 类似 Java 的 SessionFactory.openSession()
AsyncSessionLocal = async_sessionmaker(
    bind=engine,
    class_=AsyncSession,
    expire_on_commit=False,  # 提交后不清除对象缓存（方便继续使用）
)


# ===== 依赖注入函数 =====
# FastAPI 的 Depends 会调用这个函数，在请求生命周期内管理 session
# 类似 Java Spring 的 @Transactional + EntityManager
async def get_database():
    """获取数据库会话的依赖注入函数"""
    async with AsyncSessionLocal() as session:
        try:
            yield session          # 把 session 交给调用方（路由函数）
            await session.commit() # 正常结束 → 提交事务
        except Exception:
            await session.rollback()  # 异常 → 回滚事务
            raise
        finally:
            await session.close()  # 无论如何都关闭会话
