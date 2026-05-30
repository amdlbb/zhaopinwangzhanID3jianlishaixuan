# FastAPI Demo

## 启动

### 方式 1：双击 run.bat
### 方式 2：终端手动
```bash
cd fastapi-demo
venv\Scripts\activate
uvicorn main:app --reload
```

## 访问
- API 文档: http://localhost:8000/docs
- 替代文档: http://localhost:8000/redoc
- 健康检查: http://localhost:8000/health

## 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | / | 欢迎页 |
| GET | /items | 获取所有商品 |
| GET | /items/{id} | 获取单个商品 |
| POST | /items | 创建商品 |
| PUT | /items/{id} | 更新商品 |
| DELETE | /items/{id} | 删除商品 |
