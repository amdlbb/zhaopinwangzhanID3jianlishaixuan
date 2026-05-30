"""
main.py - 项目启动入口（顶层快捷方式）
========================================

直接运行：
  python main.py                  # 开发模式（热重载）

或者（推荐）：
  uvicorn app.main:app --reload   # 标准方式

两种方式等价，顶部文件只是指向 app/main.py 的快捷入口。
"""

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)
