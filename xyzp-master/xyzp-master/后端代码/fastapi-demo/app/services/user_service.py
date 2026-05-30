"""
user_service.py - 用户业务逻辑层
==================================

【功能】
  封装用户相关的业务操作（增删改查），调用 ORM 模型与数据库交互。
  路由层只负责 HTTP 协议，Service 层负责业务。

【类比 Java】
  @Service
  public class UserService {
      @Autowired private UserRepository userRepository;
  }

【用法】
  from app.services.user_service import UserService
  user = await UserService.create_user(db, data)

  说明：这里用类方法（@classmethod）而非实例方法，
        因为 Service 层没有状态（stateless），不需要实例化。
        也可以直接写函数，用类是为了分组清晰。
"""

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from app.models.user import User
from app.schemas.user import UserCreate, UserUpdate


class UserService:
    """用户业务逻辑"""

    @classmethod
    async def create_user(cls, db: AsyncSession, data: UserCreate) -> User:
        """
        创建用户
        :param db:   数据库会话
        :param data: 用户创建请求体
        :return:     创建的 User ORM 对象
        """
        # 1. 检查用户名是否已存在
        result = await db.execute(select(User).where(User.username == data.username))
        existing = result.scalar_one_or_none()
        if existing:
            raise ValueError(f"用户名 '{data.username}' 已存在")

        # 2. 创建新用户
        #    实际项目中密码必须哈希（如 passlib + bcrypt），这里简化演示
        user = User(
            username=data.username,
            password=data.password,  # ⚠️ 演示用，实际要加密
            email=data.email,
        )
        db.add(user)
        await db.flush()  # 刷新到数据库获取 id
        await db.refresh(user)  # 重新读取确保所有字段最新
        return user

    @classmethod
    async def get_user_by_id(cls, db: AsyncSession, user_id: int) -> User | None:
        """
        根据 ID 查询用户
        :return: User 对象或 None
        """
        result = await db.execute(select(User).where(User.id == user_id))
        return result.scalar_one_or_none()

    @classmethod
    async def get_all_users(cls, db: AsyncSession) -> list[User]:
        """获取所有用户"""
        result = await db.execute(select(User).order_by(User.id))
        return list(result.scalars().all())

    @classmethod
    async def update_user(cls, db: AsyncSession, user_id: int, data: UserUpdate) -> User | None:
        """
        更新用户信息
        :return: 更新后的 User 对象，不存在返回 None
        """
        user = await cls.get_user_by_id(db, user_id)
        if not user:
            return None

        # 只更新传了值的字段
        update_data = data.model_dump(exclude_unset=True)
        for field, value in update_data.items():
            setattr(user, field, value)

        await db.flush()
        await db.refresh(user)
        return user

    @classmethod
    async def delete_user(cls, db: AsyncSession, user_id: int) -> bool:
        """
        删除用户
        :return: 是否删除成功
        """
        user = await cls.get_user_by_id(db, user_id)
        if not user:
            return False
        await db.delete(user)
        await db.flush()
        return True
