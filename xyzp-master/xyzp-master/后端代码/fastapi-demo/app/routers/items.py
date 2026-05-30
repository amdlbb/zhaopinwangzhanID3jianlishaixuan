from fastapi import APIRouter, HTTPException
from pydantic import BaseModel

router = APIRouter()


# 数据模型
class Item(BaseModel):
    id: int
    name: str
    price: float
    is_offer: bool | None = None


# 模拟数据库
fake_db: dict[int, Item] = {
    1: Item(id=1, name="键盘", price=299.0, is_offer=False),
    2: Item(id=2, name="鼠标", price=99.0, is_offer=True),
}


@router.get("/")
async def list_items():
    """获取所有商品"""
    return list(fake_db.values())


@router.get("/{item_id}")
async def read_item(item_id: int, q: str | None = None):
    """获取单个商品，可选查询参数 q"""
    if item_id not in fake_db:
        raise HTTPException(status_code=404, detail="商品不存在")
    item = fake_db[item_id]
    if q:
        return {"item": item, "q": q}
    return item


@router.post("/")
async def create_item(item: Item):
    """创建新商品"""
    if item.id in fake_db:
        raise HTTPException(status_code=400, detail="商品 ID 已存在")
    fake_db[item.id] = item
    return {"message": "创建成功", "item": item}


@router.put("/{item_id}")
async def update_item(item_id: int, item: Item):
    """更新商品"""
    if item_id not in fake_db:
        raise HTTPException(status_code=404, detail="商品不存在")
    fake_db[item_id] = item
    return {"message": "更新成功", "item": item}


@router.delete("/{item_id}")
async def delete_item(item_id: int):
    """删除商品"""
    if item_id not in fake_db:
        raise HTTPException(status_code=404, detail="商品不存在")
    del fake_db[item_id]
    return {"message": "删除成功"}
