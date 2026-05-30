"""
user.py - 用户 API 路由
==========================

【功能】
  用户 CRUD 的 HTTP 接口定义。只处理 HTTP 请求/响应，业务逻辑委托给 Service。

【类比 Java】
  @RestController
  @RequestMapping("/api/users")
  public class UserController {
      @Autowired private UserService userService;
  }

【用法】
  router = APIRouter(prefix="/users", tags=["用户管理"])
  app.include_router(router)
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.database import get_database
from app.schemas.user import UserCreate, UserUpdate, UserResponse
from app.schemas.common import Result
from app.services.user_service import UserService

# 创建路由对象，所有接口自动加上 /users 前缀
router = APIRouter(prefix="/users", tags=["用户管理"])


@router.get("/", response_model=Result)
async def list_users(db: AsyncSession = Depends(get_database)):
    """
    获取所有用户列表
    GET /users
    """
    users = await UserService.get_all_users(db)
    # 用列表推导将 ORM 模型转为 Pydantic 响应模型
    return Result(data=[UserResponse.model_validate(u) for u in users])


@router.get("/{user_id}", response_model=Result)
async def get_user(user_id: int, db: AsyncSession = Depends(get_database)):
    """
    获取单个用户
    GET /users/1

    【对比 Java】
    @GetMapping("/{id}")
    public Result<UserVO> getUser(@PathVariable Long id)
    """
    user = await UserService.get_user_by_id(db, user_id)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"用户 {user_id} 不存在",
        )
    return Result(data=UserResponse.model_validate(user))


@router.post("/", response_model=Result, status_code=status.HTTP_201_CREATED)
async def create_user(
    user_data: UserCreate,
    db: AsyncSession = Depends(get_database),
):
    """
    创建用户
    POST /users
    Body: {"username": "螯", "password": "123456", "email": "ao@example.com"}

    【对比 Java】
    @PostMapping("/")
    public Result<UserVO> createUser(@RequestBody @Valid UserCreateDTO dto)
    """
    try:
        user = await UserService.create_user(db, user_data)
        return Result(
            code=201,
            message="用户创建成功",
            data=UserResponse.model_validate(user),
        )
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e),
        )


@router.put("/{user_id}", response_model=Result)
async def update_user(
    user_id: int,
    user_data: UserUpdate,
    db: AsyncSession = Depends(get_database),
):
    """
    更新用户信息
    PUT /users/1
    Body: {"email": "new@example.com"}

    【对比 Java】
    @PutMapping("/{id}")
    public Result<UserVO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO dto)
    """
    user = await UserService.update_user(db, user_id, user_data)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"用户 {user_id} 不存在",
        )
    return Result(data=UserResponse.model_validate(user))


@router.delete("/{user_id}", response_model=Result)
async def delete_user(user_id: int, db: AsyncSession = Depends(get_database)):
    """
    删除用户
    DELETE /users/1

    【对比 Java】
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id)
    """
    success = await UserService.delete_user(db, user_id)
    if not success:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"用户 {user_id} 不存在",
        )
    return Result(message="用户删除成功")
