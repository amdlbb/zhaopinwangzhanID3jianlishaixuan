# AI 客服前端 UI 设计

> 文档位置：`前端代码/xyzp5.21/src/components/FloatButton/`
> 对接 API 文档：`docs/chat-api.md`

---

## 1. 整体布局

```
┌─────────────────────────────────────────────┬──────────────────┐
│                                             │                  │
│                                             │  ✕  AI 客服      │  ← PanelHeader
│              主页面                         │──────────────────│
│         （求职/招聘内容）                     │  历史  │  当前   │  ← 左右两栏
│                                             │───────│─────────│
│                                             │┌─────┐│         │
│                                             ││云才  ││ ┌─────┐ │
│                                             ││科技  ││ │简   │ │
│                                             ││的校  ││ │历投 │ │
│                                             ││招流  ││ │递后 │ │
│                                             ││程    ││ │多久 │ │
│                                             ││      ││ │收到 │ │
│                                             ││2条   ││ │回复?│ │
│                                             ││10:05 ││ └─────┘ │
│                                             │└─────┘│         │
│                                             │┌─────┐│ ┌─────┐ │
│                                             ││简历  ││ │通   │ │
│                                             ││投递  ││ │常投 │ │
│                                             ││后多  ││ │递后 │ │
│                                             ││久收  ││ │3-5  │ │
│                                             ││到    ││ │个工 │ │
│                                             ││      ││ │作日 │ │
│                                             ││4条   ││ │内会 │ │
│                                             ││14:35 ││ │收.. │ │
│                                             │└─────┘│ └─────┘ │
│                                             │┌─────┐│ 📎参考  │
│                                             ││面试  ││ 来源    │
│                                             ││通知  ││        │
│                                             ││时间  ││        │
│                                             ││      ││        │
│                                             ││3条   ││        │
│                                             ││09:12 ││        │
│                                             │└─────┘│        │
│                                             │───────│─────────│
│                                             │       │输入...   │
│                                             │       │ [发送]   │
│                           ┌───┐             │       │         │
│                           │ 💬│             │       │         │
│                           └───┘             │       │         │
└─────────────────────────────────────────────┴───────┴─────────┘
```

### 面板参数

| 属性 | 值 |
|------|-----|
| 面板总宽 | 460px（比之前宽，因要容纳两栏） |
| 左栏宽（历史会话） | 140px |
| 右栏宽（当前对话） | 320px |
| 高度 | 100vh |
| 定位 | `position: fixed; right: 0; top: 0;` |
| 动画 | `transform: translateX(100%)` → `translateX(0)`, CSS transition `0.3s ease` |
| z-index | 高于主页面内容 |

> 左右两栏用 flex 布局，左栏固定 140px，右栏 flex:1 占剩余宽度。
> 两栏之间有一条 1px 分隔线。

---

## 2. 组件树

```
FloatButton/index.vue           ← 右下角悬浮按钮，点击展开/收起面板
  └── AIChatPanel.vue           ← 主面板容器（flex 行布局 + 滑出动画）
        ├── PanelHeader         ← "AI 客服" 标题 + ✕ 关闭按钮（全宽）
        ├── MainContent         ← flex 行容器
        │     ├── SessionList   ← 左栏：历史会话列表（140px，可滚动）
        │     │     └── SessionItem (×N)  ← 单条会话
        │     │           ├── 标题（截断显示）
        │     │           ├── 消息数 + 时间
        │     │           └── 删除按钮 ✕（hover 显示）
        │     │
        │     └── ChatView      ← 右栏：当前对话（flex:1）
        │           ├── ChatArea      ← 消息列表（可滚动）
        │           │     └── MessageBubble (×N)
        │           │           └── SourceCollapse  ← 来源引用折叠
        │           └── InputArea     ← 输入框 + 发送按钮
        │
        └── LoginOverlay       ← 未登录时覆盖面板的提示层
```

---

## 3. 登录与未登录状态

```
未登录时 ── 整个面板覆盖一层半透明提示：

┌────────────────────────────────────┐
│ ✕  AI 客服                         │
│────────────────────────────────────│
│                                    │
│           🔒                       │
│      请先登录后使用                 │
│     AI 智能客服功能                 │
│                                    │
│      [去登录]                       │
│                                    │
│    还未注册？ 立即注册 →             │
│                                    │
│────────────────────────────────────│
│                                    │
└────────────────────────────────────┘

已登录时 ── 正常显示左右两栏界面。
```

**登录状态获取**：页面 mount 时调用 `/api/authorization/check-login`（通过 Gateway），
返回 `isLoggedIn` 存入 Vuex/Pinia。面板打开时根据此状态决定显示内容。

---

## 4. 左栏 — 历史会话列表

```
┌──────────────────────────────────────────────┐
│  ✕  AI 客服                                   │
│──────────────────────────────────────────────│
│ ┌─────────┐  ┌──────────────────────────────┐│
│ │ 云才科   │  │ ╔══════════════════════════╗ ││
│ │ 技的校   │  │ ║  简历投递后多久    ← 用户 ║ ││
│ │ 招流程   │  │ ║  会收到回复？             ║ ││
│ │         │  │ ╚══════════════════════════╝ ││
│ │ 2条     │  │                              ││
│ │ 10:05   │  │ ┌──────────────────────────┐ ││
│ └─────────┘  │ │ 通常投递后，系统会自动    │ ││
│ ┌─────────┐  │ │ 进行简历筛选，3-5个工作    │ ││
│ │ 简历投   │  │ │ 日内会收到通知。           │ ││
│ │ 递后多   │  │ │                          │ ││
│ │ 久收到   │  │ │ 📎 参考来源              │ ││
│ │         │  │ └──────────────────────────┘ ││
│ │ 4条     │  │                              ││
│ │ 14:35   │  │ ┌──────────────────────────┐ ││
│ └─────────┘  │ │ 那可以同时投多个吗？      │ ││
│ ┌─────────┐  │ └──────────────────────────┘ ││
│ │ 面试通   │  │                              ││
│ │ 知时间   │  │                              ││
│ │         │  │──────────────────────────────││
│ │ 3条     │  │  输入问题...            [发送]││
│ │ 09:12   │  │                              ││
│ └─────────┘  └──────────────────────────────┘│
└──────────────────────────────────────────────┘
```

### 列表项样式

```
┌─────────────────┐
│ 简历投递后多久   │  ← 标题（最多显示 2 行，溢出截断）
│ 收到             │
│ 4条  14:35      │  ← 消息数 + 简短时间
│                 │
│           ✕     │  ← 删除按钮（hover 显示/常显）
└─────────────────┘

高亮状态：背景色轻微变化 + 左侧 3px 蓝色竖条

┌─────────────────┐
│▌简历投递后多久   │  ← 蓝色左边框 3px
│▌收到             │
│▌4条  14:35      │
│▌                 │
│▌           ✕     │
└─────────────────┘
```

### 交互逻辑

| 操作 | 行为 |
|------|------|
| 点击某条会话 | 切换当前对话到该 session → 调 `getSession` 加载消息 |
| 高亮 | 当前选中的 session 有蓝色左边框 + 浅色背景 |
| 删除 | 调 `removeSession` → 刷新列表 → 如果删除的是当前会话则清空右栏 |
| 滚动 | 列表超出高度时滚动 |
| 打开面板 | 自动调用 `searchSession` 加载列表，默认选中最新一条 |

---

## 5. 右栏 — 当前对话

### 消息气泡

```
AI 消息（左对齐，浅灰背景，圆角 8px）：

  ┌──────────────────────────┐
  │ 简历投递后，系统会自动    │
  │ 进行简历筛选，通常        │
  │ 3-5 个工作日内会收到通知。│
  │                          │
  │ 📎 参考来源               │  ← 点击展开
  │   ┌──────────────────┐   │
  │   │ 云才科技AI客服RAG │   │
  │   │ 文档.txt          │   │
  │   │ 相似度: 0.88      │   │
  │   └──────────────────┘   │
  └──────────────────────────┘

用户消息（右对齐，品牌蓝色，圆角 8px）：

         ┌──────────────────────┐
         │ 简历投递后多久        │
         │ 会收到回复？          │
         └──────────────────────┘
```

### 来源折叠（SourceCollapse）

```
📎 参考来源                              ← 点击展开/收起
├── 云才科技AI客服RAG文档.txt    (0.88)   ← sources[0]
├── 投递流程FAQ.pdf              (0.72)   ← sources[1]
└── 校招常见问题.docx            (0.65)   ← sources[2]
```

### 输入区域

```
───────────────────────────────────────
│  输入问题...                    [发送] │
───────────────────────────────────────

- 输入框：单行 textarea，支持 Enter 发送
- 发送按钮：蓝色，禁用状态（空文本时 / 流式生成中）
- 流式生成中：发送按钮显示加载动画，输入框置灰
```

---

## 6. SSE 对接

```javascript
// 发送消息（使用 fetch + ReadableStream，因为需要 token header）
function sendMessage(question, sessionId) {
  let url = '/api/ai/chat/sendMessage?question=' + encodeURIComponent(question);
  if (sessionId) url += '&session_id=' + sessionId;

  fetch(url, {
    headers: { 'token': localStorage.getItem('token') }
  }).then(response => {
    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    let buffer = '';

    function readStream() {
      reader.read().then(({ done, value }) => {
        if (done) return;
        buffer += decoder.decode(value, { stream: true });
        // 按 SSE 格式：event:\ndata:\n\n 分割
        const parts = buffer.split('\n\n');
        buffer = parts.pop(); // 最后一段可能不完整
        for (const part of parts) {
          const lines = part.split('\n');
          let eventType = '';
          let dataStr = '';
          for (const line of lines) {
            if (line.startsWith('event: ')) eventType = line.slice(7);
            if (line.startsWith('data: ')) dataStr = line.slice(6);
          }
          if (!dataStr) continue;
          const data = JSON.parse(dataStr);
          if (eventType === 'meta') {
            // 记录 session_id
            this.currentSessionId = data.session_id;
            // 若 data.code !== 200，显示错误
          } else if (eventType === 'token') {
            // 追加到当前 AI 气泡
            this.appendToken(data.token);
          } else if (eventType === 'done') {
            // 显示 sources，流结束
            this.showSources(data.sources);
          }
        }
        readStream();
      });
    }
    readStream();
  });
}

// 查询会话列表
function fetchSessions() {
  fetch('/api/ai/chat/searchSession', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'token': localStorage.getItem('token')
    },
    body: '{}'
  }).then(r => r.json()).then(res => {
    if (res.code === 200) this.sessions = res.data.sessions;
  });
}

// 查询会话详情
function fetchSessionDetail(sessionId) {
  fetch('/api/ai/chat/getSession', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'token': localStorage.getItem('token')
    },
    body: JSON.stringify({ session_id: sessionId })
  }).then(r => r.json()).then(res => {
    if (res.code === 200) this.messages = res.data.messages;
  });
}

// 删除会话
function removeSession(sessionId) {
  fetch('/api/ai/chat/removeSession', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'token': localStorage.getItem('token')
    },
    body: JSON.stringify({ session_id: sessionId })
  }).then(r => r.json()).then(res => {
    if (res.code === 200) this.fetchSessions();
  });
}
```

> ⚠️ EventSource 不支持自定义 header。sendMessage 需要带 `token` header，
> 因此必须用 `fetch + ReadableStream` 替代 EventSource。

---

## 7. 组件文件清单

```
前端代码/xyzp5.21/src/
├── components/
│   └── FloatButton/
│       ├── index.vue              ← 悬浮按钮入口（已有，需补充逻辑）
│       ├── Chat/
│       │   └── index.vue          ← AI 客服主面板（已有空壳 → 重构）
│       ├── SessionList/
│       │   └── index.vue          ← 左栏：会话列表（新建）
│       └── ChatSession/
│           └── index.vue          ← 会话详情组件（已有空壳 → 重构）
│
├── api/
│   └── ai.js                      ← AI 客服 API 封装（新增）
│       ├── sendMessage()          → fetch + SSE 历史会话
│       ├── fetchSessions()        → POST searchSession
│       ├── fetchSessionDetail()   → POST getSession
│       └── removeSession()        → POST removeSession
│
└── store/
    └── ai.js                      ← AI 客服状态管理（新增）
        ├── isPanelOpen            ← 面板展开/收起
        ├── isLoggedIn             ← 登录状态
        ├── sessions[]             ← 会话列表
        ├── currentSessionId       ← 当前选中的会话 ID
        ├── messages[]             ← 当前会话的消息列表
        └── isStreaming            ← 流式生成中（输入框禁用/发送按钮 loading）
```

---

## 8. 数据流总览

```
[用户打开面板]
    │
    ├─ 未登录 → 显示 LoginOverlay
    │             └─ 点击"去登录" → 跳转登录页
    │
    └─ 已登录 →
         ├─ fetchSessions()              → 加载会话列表到左栏
         ├─ 选中最新一条 session
         │    └─ fetchSessionDetail(id)  → 加载消息到右栏
         │
         ├─ [用户点击左栏某条会话]
         │    └─ fetchSessionDetail(id)  → 切换右栏消息
         │
         ├─ [用户输入并发送]
         │    └─ sendMessage()           → fetch + ReadableStream
         │         ├─ meta  → 记录 session_id
         │         ├─ token → 追加到 AI 气泡
         │         └─ done  → 显示 sources
         │
         └─ [用户点击 ✕ 删除]
              └─ removeSession(id) → 刷新列表
```
