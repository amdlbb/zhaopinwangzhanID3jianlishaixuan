# AI 客服 API 接口文档

> 项目：`C:\bs\xyzp-master\xyzp-master\后端代码\fastapi-demo`
> 基路径：`/api/ai`（前端通过 Gateway 8888 访问，StripPrefix=2 后转 FastAPI 8000）
>
> **认证说明**：以下接口中标注"需登录"的，前端必须在请求头携带 `token: <jwt>`。
> 临时会话免登录，不走 JWT 校验。

---

## 1. 临时会话

### `POST /api/ai/chat/tempAsk`

**功能**：用户发送一条问题，AI 基于 RAG 知识库实时回答。**数据不落库，上下文保存在内存中**（InMemoryChatHistory），前端传入 `session_id` 可延续对话。

**响应格式**：SSE（Server-Sent Events）流式响应。前端应使用 `EventSource` API 接收。

**执行逻辑**：收到问题 → 查 InMemoryChatHistory 拿历史 → Chroma 检索相关知识 → **逐 token 推送给前端** → 写入 InMemoryChatHistory → 推 sources 后关闭连接。

**请求示例**（首次，无 session_id）：
```json
{
    "question": "云才科技是做什么的？"
}
```

**请求示例**（续聊，传 session_id）：
```json
{
    "question": "他们的产品有哪些？",
    "session_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

**SSE 响应格式**：
```
HTTP/1.1 200 OK
Content-Type: text/event-stream
Cache-Control: no-cache
Connection: keep-alive

event: meta
data: {"code": 200, "msg": null, "session_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"}

event: token
data: {"token": "云才科技是"}

event: token
data: {"token": "一家人工智能"}

event: token
data: {"token": "驱动的招聘"}

event: token
data: {"token": "科技公司..."}

event: done
data: {"sources": [{"title": "云才科技AI客服RAG文档.txt", "score": 0.92, "snippet": "云才科技成立于2023年，是一家专注于校园招聘场景的AI科技公司..."}]}
```

**SSE 事件表**：

| 事件名 | 时机 | 前端处理 |
|--------|------|---------|
| `meta` | 连接建立后，开始生成前 | 记录 session_id，创建聊天气泡 |
| `token` | 每收到一个 LLM token | `displayText += data.token` |
| `done` | 完整回答 + sources 就绪 | 显示 sources，关闭 EventSource |

**前端示例**：
```javascript
const es = new EventSource('/api/ai/chat/tempAsk?question=' + encodeURIComponent(question));
es.addEventListener('meta', e => { this.sessionId = JSON.parse(e.data).session_id; });
es.addEventListener('token', e => this.displayText += JSON.parse(e.data).token);
es.addEventListener('done', e => { this.sources = JSON.parse(e.data).sources; es.close(); });
```

> ⚠️ 此接口为 SSE 流式响应，前端**不能**使用 axios/fetch POST 发送，请使用 `EventSource` API。请求参数通过 URL query 传递 `question` 和 `session_id` 字段。

---

## 2. 发送历史消息

### `POST /api/ai/chat/sendMessage`

**功能**：用户在历史会话中发送一条消息。**需登录**。消息持久化到 MySQL，同时缓存到 Redis。无 `session_id` 时自动创建新会话。

**响应格式**：SSE（Server-Sent Events）流式响应。前端应使用 `EventSource` API 接收。

**执行逻辑**：校验 JWT → 无 session_id 则生成雪花 ID 建会话行 → ChatHistoryStore 取历史 → Chroma 检索 → **逐 token 推送给前端** → 等待流结束后 MySQL 写入用户消息+完整AI消息 → Redis 缓存更新 → 推 sources 后关闭连接。

**请求示例**（新建会话，首次发消息）：
```json
{
    "question": "简历投递后多久会收到回复？"
}
```

**请求示例**（续聊，传入已有的 session_id）：
```json
{
    "question": "那可以同时投递多个职位吗？",
    "session_id": 8732390091937312768
}
```

**SSE 响应格式**：
```
HTTP/1.1 200 OK
Content-Type: text/event-stream
Cache-Control: no-cache
Connection: keep-alive

event: meta
data: {"code": 200, "msg": null, "session_id": 8732390091937312768}

event: token
data: {"token": "通常投递后"}

event: token
data: {"token": " 3-5 个工作日"}

event: token
data: {"token": "内会收到审核"}

event: token
data: {"token": "结果..."}

event: done
data: {"sources": [{"title": "云才科技AI客服RAG文档.txt", "score": 0.88, "snippet": "简历投递后，HR 会在 3-5 个工作日内完成初审..."}]}
```

**SSE 事件表**：

| 事件名 | 时机 | 前端处理 |
|--------|------|---------|
| `meta` | 连接建立后，开始生成前 | 记录 session_id，创建聊天气泡 |
| `token` | 每收到一个 LLM token | `displayText += data.token` |
| `done` | 完整回答 + sources 就绪 | 显示 sources，关闭 EventSource |

**前端示例**：
```javascript
const es = new EventSource('/api/ai/chat/sendMessage?question=' + encodeURIComponent(question));
es.addEventListener('meta', e => { const d = JSON.parse(e.data); this.sessionId = d.session_id; });
es.addEventListener('token', e => this.displayText += JSON.parse(e.data).token);
es.addEventListener('done', e => { this.sources = JSON.parse(e.data).sources; es.close(); });
```

> ⚠️ 此接口为 SSE 流式响应，前端**不能**使用 axios/fetch POST 发送，请使用 `EventSource` 或 fetch + `ReadableStream`。请求参数通过 URL query 传递 `question` 和 `session_id` 字段。

---

## 3. 查询会话列表

### `POST /api/ai/chat/searchSession`

**功能**：查询当前登录用户的所有历史会话摘要列表。**需登录**。按最后更新时间倒序排列。

**执行逻辑**：校验 JWT → 从 request.state 取 user_id → MySQL 查 chat_sessions 倒序 → 返回列表。

**请求示例**：
```json
{}
```

**响应示例**：
```json
{
    "code": 200,
    "msg": null,
    "data": {
        "sessions": [
            {
                "id": 8732390091937312768,
                "title": "简历投递后多久会收到回复？",
                "message_count": 4,
                "created_at": "2026-06-05T14:30:00",
                "updated_at": "2026-06-05T14:35:00"
            },
            {
                "id": 8732390091937312767,
                "title": "云才科技的校招流程",
                "message_count": 2,
                "created_at": "2026-06-04T10:00:00",
                "updated_at": "2026-06-04T10:05:00"
            }
        ],
        "total": 2
    }
}
```

---

## 4. 查询会话详情

### `POST /api/ai/chat/getSession`

**功能**：查询单条历史会话的完整消息内容。**需登录**。仅返回当前用户所属的会话。

**执行逻辑**：校验 JWT → 校验 session_id 归属 → Redis 缓存查 → 未命中查 MySQL → 返回会话摘要+消息列表。

**请求示例**：
```json
{
    "session_id": 8732390091937312768
}
```

**响应示例**：
```json
{
    "code": 200,
    "msg": null,
    "data": {
        "session": {
            "id": 8732390091937312768,
            "title": "简历投递后多久会收到回复？",
            "message_count": 4,
            "created_at": "2026-06-05T14:30:00",
            "updated_at": "2026-06-05T14:35:00"
        },
        "messages": [
            {
                "id": 1,
                "role": "user",
                "content": "简历投递后多久会收到回复？",
                "sources": [],
                "created_at": "2026-06-05T14:30:00"
            },
            {
                "id": 2,
                "role": "assistant",
                "content": "通常投递后 3-5 个工作日内会收到审核结果...",
                "sources": [
                    {
                        "title": "云才科技AI客服RAG文档.txt",
                        "score": 0.88,
                        "snippet": "简历投递后，HR 会在 3-5 个工作日内完成初审..."
                    }
                ],
                "created_at": "2026-06-05T14:30:05"
            }
        ]
    }
}
```

---

## 5. 删除会话

### `POST /api/ai/chat/removeSession`

**功能**：物理删除一条历史会话及其所有消息。**需登录**。仅删除当前用户所属的会话。

**执行逻辑**：校验 JWT → 校验 session_id 归属 → 物理删除 chat_messages 行 + chat_sessions 行 + 清理 Redis 缓存。

**请求示例**：
```json
{
    "session_id": 8732390091937312768
}
```

**响应示例**：
```json
{
    "code": 200,
    "msg": "删除成功",
    "data": null
}
```

---

## 统一响应格式

所有接口遵循 `app/schemas/common.py` 定义的统一格式：

```json
{
    "code": 200,
    "msg": null,
    "data": { ... }
}
```

| 字段 | 说明 |
|------|------|
| `code` | 200=成功，201=业务错误，500=服务端异常 |
| `msg`  | 错误时返回错误描述，成功时为 null |
| `data` | 成功时的业务数据，结构见各接口响应示例 |

> ⚠️ **注意**：`tempAsk` 和 `sendMessage` 两个接口**不使用**此统一 JSON 响应格式。
> 它们使用 **SSE（text/event-stream）** 格式，协议见各自章节。

---

## 请求路径对照表

| 前端发往 Gateway | 经过 StripPrefix=2 | FastAPI 实际接收 |
|------------------|-------------------|-----------------|
| `/api/ai/chat/tempAsk` | → | `/chat/tempAsk` | ⚡ SSE 流式 |
| `/api/ai/chat/sendMessage` | → | `/chat/sendMessage` | ⚡ SSE 流式 |
| `/api/ai/chat/searchSession` | → | `/chat/searchSession` |
| `/api/ai/chat/getSession` | → | `/chat/getSession` |
| `/api/ai/chat/removeSession` | → | `/chat/removeSession` |
