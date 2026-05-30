"""
common.py - 通用响应模型
=========================

【功能】
  定义统一的 API 响应格式，以及通用请求/响应结构。
  所有路由应尽量使用统一返回格式，方便前端处理。

【类比 Java】
  public class Result<T> {
      private int code;
      private String message;
      private T data;
  }
"""

from typing import Any, TypeVar, Generic
from pydantic import BaseModel

# 泛型类型变量（类似 Java 的 T）
T = TypeVar("T")


class Result(BaseModel):
    """
    统一 API 响应格式
    所有接口返回这个结构，方便前端统一处理
    """
    code: int = 200            # 状态码（与 HTTP 状态码一致）
    message: str = "success"   # 提示信息
    data: Any = None           # 实际数据


class PaginatedResult(Result):
    """
    分页响应格式
    继承 Result，额外包含分页信息
    """
    total: int = 0             # 总记录数
    page: int = 1              # 当前页码
    page_size: int = 20        # 每页条数
