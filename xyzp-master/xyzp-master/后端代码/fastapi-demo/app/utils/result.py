from typing import Optional, Any
from pydantic import BaseModel, ConfigDict

class Result(BaseModel):
    """统一响应模型（非泛型简化版）"""
    code: int
    msg: Optional[str] = None
    data: Optional[Any] = None

    # 开启 ORM 模式，支持 SQLAlchemy 等直接传入
    model_config = ConfigDict(from_attributes=True)

    @classmethod
    def success(cls, data: Any = None, msg: Optional[str] = None):
        """成功响应，状态码 200"""
        return cls(code=200, msg=msg, data=data)

    @classmethod
    def error(cls, msg: str, code: int = 201):
        """错误响应，默认状态码 201"""
        return cls(code=code, msg=msg, data=None)

'''
# 查数据库
orm_list = db.query(Resume).all()

# 转成 Pydantic 模型
data = [ResumeSchema.model_validate(r) for r in orm_list]

# 包装成统一响应
return Result.success(data=data, msg="查询成功")
'''