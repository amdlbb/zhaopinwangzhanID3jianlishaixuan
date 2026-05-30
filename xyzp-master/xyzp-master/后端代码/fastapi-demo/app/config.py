"""
config.py - 应用配置
=======================

【功能】
  集中管理所有可配置项：数据库、服务端口、JWT 密钥等。
  这里用 Python 类的形式硬编码配置，正式项目通常会从环境变量 / .env 文件读取。

【类比 Java】
  application.yml 或 application.properties

【用法】
  from app.config import settings
  settings.DATABASE_URL  # 获取数据库 URL
"""


class Settings:
    """应用全局配置"""
    # 应用信息
    APP_TITLE: str = "FastAPI Demo"
    APP_VERSION: str = "1.0.0"
    APP_DESCRIPTION: str = "FastAPI 标准项目结构示例"

    # 服务端口
    HOST: str = "0.0.0.0"
    PORT: int = 8000

    # 数据库配置（硬编码，后续可以从环境变量读取）
    DATABASE_URL: str = "mysql+aiomysql://root:123456@localhost:3306/kfzs"
    DATABASE_ECHO: bool = True      # 开发阶段打印 SQL
    DATABASE_POOL_SIZE: int = 10    # 连接池大小
    DATABASE_MAX_OVERFLOW: int = 20 # 最大溢出连接数

    REDIS_URL: str = "localhost"
    REDIS_PORT: int = 6379
    REDIS_DB: int = 0


# 创建全局配置实例（类似 Spring 的 @ConfigurationProperties 单例）
settings = Settings()
