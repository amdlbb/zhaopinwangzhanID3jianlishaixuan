"""
user.py - 用户请求/响应模型
=============================

【功能】
  定义用户相关的 Pydantic 模型，用于：
  - 请求体验证（接收前端传参）
  - 响应体序列化（返回数据）
  - API 文档自动生成（Swagger UI 的 Schema）

【类比 Java】
  请求体  ← UserCreateDTO (public class UserCreateDTO { @NotBlank String name; })
  响应体  ← UserVO     (public class UserVO { private Long id; private String name; })

【用法】
  @router.post("/users", response_model=UserResponse)
  async def create_user(user: UserCreate):
      ...
"""

from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime


class UserCreate(BaseModel):
    """
    创建用户的请求体
    前端 POST /users 时传这个 JSON
    """
    username: str = Field(..., min_length=2, max_length=50, description="用户名")
    password: str = Field(..., min_length=6, max_length=255, description="密码")
    email: Optional[str] = Field(None, description="邮箱")


class UserUpdate(BaseModel):
    """
    更新用户的请求体
    所有字段可选，只传需要修改的字段（PATCH 语义）
    """
    password: Optional[str] = Field(None, min_length=6, max_length=255, description="新密码")
    email: Optional[str] = Field(None, description="新邮箱")
    is_active: Optional[bool] = Field(None, description="是否启用")


class UserResponse(BaseModel):
    """
    用户响应体
    返回给前端时隐藏 password 字段
    """
    id: int
    username: str
    email: Optional[str] = None
    is_active: bool = True
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None

    # 告诉 Pydantic：可以从 ORM 模型自动转换
    model_config = {"from_attributes": True}
