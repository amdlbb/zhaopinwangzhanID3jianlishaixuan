# utils/redis_client.py
import redis

class RedisConfig:
    client = redis.Redis(
        host='127.0.0.1',
        port=6379,
        db=0,
        decode_responses=True   # 自动解码为字符串，方便使用
    )