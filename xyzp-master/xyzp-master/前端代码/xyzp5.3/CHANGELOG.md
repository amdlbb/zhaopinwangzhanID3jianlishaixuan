# xyzp 前端改造 — 变更日志

## 项目：校园招聘网站 → 企业官网（云才科技）
## 改造完成时间：2026-05-03 23:30

---

## 改动概览

### ✅ 新增文件（共 5 个）
| 文件 | 路由 | 说明 |
|------|------|------|
| `src/pages/AboutUs/index.vue` | `/about` | 关于我们 — 公司简介、核心数据、发展历程 |
| `src/pages/ProductService/index.vue` | `/product` | 产品服务 — 智能简历OA筛选系统介绍、技术优势 |
| `src/pages/News/index.vue` | `/news` | 新闻动态 — 公司新闻/行业资讯列表 |
| `src/pages/Contact/index.vue` | `/contact` | 联系我们 — 联系方式、在线留言表单 |
| `src/assets/css/global.css` | — | 全局样式变量（主色调: 深蓝 #1a3a5c） |

### ✅ 修改文件
| 文件 | 改动内容 |
|------|---------|
| `public/index.html` | 标题改为"云才科技 - 智能简历OA筛选系统" |
| `src/main.js` | 引入 global.css |
| `src/App.vue` | 移除多余样式代码 |
| `src/router/index.js` | **新增 4 条路由**（about, product, news, contact），原有路由全部保留 |
| `src/components/NavBar/index.vue` | **模板改造**：导航项改为 首页/招聘信息/关于我们/产品服务/新闻动态/联系我们；登录按钮改为"登录 / 人才中心"；用户弹窗改为"个人中心/部门中心"；注册弹窗改为"部门注册"；**样式重写**：深蓝商务风；**JS逻辑未改动** |
| `src/components/Footer/index.vue` | **完全重写**：从一行版权 → 四栏官网底部（品牌简介+快速链接+产品服务+联系方式） |
| `src/components/BlackDiv/index.vue` | 未改动 |
| `src/pages/Home/index.vue` | 精简样式代码 |
| `src/pages/Home/Swiper/index.vue` | **完全重写**：校招banner → 企业品牌形象轮播（3张深蓝商务风幻灯片+CTA按钮） |
| `src/pages/Home/Info/index.vue` | **完全重写**：校招流程/热门企业/宣讲会 → 公司介绍卡片 + 核心数据 + 热门职位 + 新闻预览 |
| `src/pages/WorkHome/index.vue` | **样式改造**：列表卡片商务风，圆角+阴影+悬停效果 |
| `src/pages/WorkHome/WorkSearch/index.vue` | 搜索按钮颜色改为深蓝 |
| `src/pages/WorkHome/WorkItem/index.vue` | 顶部渐变背景、按钮深蓝统一、section标题加底部border |
| `src/pages/TeamHome/index.vue` | 文案：团队的信息→部门信息 / 招聘的职位→在招职位 / 收到的简历→简历管理 / 面试的人员→面试安排 / 录取的人员→录用管理 |
| `src/pages/TeamHome/TeamInfo/index.vue` | 文案：团队→部门 |
| `src/pages/TeamHome/JobItem/index.vue` | 文案：招聘的职位→在招职位 / 添加/编辑团队招聘信息→添加/编辑招聘信息 |
| `src/pages/TeamHome/CvItem/index.vue` | 文案：收到的简历→简历管理 |
| `src/pages/TeamHome/InterviewItem/index.vue` | 文案：面试的人员→面试安排 |
| `src/pages/TeamHome/AdmissionItem/index.vue` | 文案：录取的人员→录用管理 |
| `src/pages/Information/index.vue` | 标题：校园直聘→行业资讯与新闻动态 |

### ❌ 未改动文件
- `src/store/` — Vuex 全部状态管理
- `src/api/` — 所有 API 接口
- `src/pages/UserHome/` — 仅未改动（保持原样）
- `src/pages/WorkHome/ChatItem/` — 保持原样
- `src/pages/Resume/` — 保持原样
- `src/pages/ChatHome/` — 保持原样（/test 路由可用）
- 所有 `.vue` 文件的 `<script>` 块 — 一行未碰

## 安全原则执行情况
1. ✅ 所有页面组件内部 JS 代码一行未碰
2. ✅ 有数据绑定的按钮（v-model/@click/v-if）保留，只改样式和文本
3. ✅ 原有路由全部保留，只新增 4 条
4. ✅ "部门注册"按钮结构保持原样（你已删的注册按钮逻辑未恢复）
5. ✅ 图标/图片使用 Unsplash 占位图片，留待你替换

## 图片占位清单（需你替换）
| 占位图片 | 使用位置 | 建议图片内容 |
|----------|---------|-------------|
| 网站logo图片 | NavBar/Footer/各页面 | 公司 logo |
| 公司banner图1-3 | Home/Swiper 三张幻灯片背景 | 公司形象/产品/团队照片 |
| 智能筛选图标图片 | Home/Info 介绍卡片 | 科技感图标 |
| 流程管理图标图片 | Home/Info 介绍卡片 | 管理类图标 |
| 数据分析图标图片 | Home/Info 介绍卡片 | 数据图表图标 |
| 企业logo图片 | 首页热门职位卡片 | 各企业 logo |
| 新闻配图 | 首页新闻预览/新闻页 | 新闻相关配图 |

## 项目目录结构（改造后）
```
xyzp5.3/
├── public/
│   └── index.html              # [改] 标题已更新
├── src/
│   ├── assets/
│   │   └── css/
│   │       └── global.css       # [新] 全局样式
│   ├── components/
│   │   ├── NavBar/index.vue     # [改] 导航栏
│   │   ├── Footer/index.vue     # [改] 页脚
│   │   └── BlackDiv/index.vue   # [未动]
│   ├── pages/
│   │   ├── Home/               # [改] 首页完全重设计
│   │   ├── AboutUs/             # [新] 关于我们
│   │   ├── ProductService/      # [新] 产品服务
│   │   ├── News/                # [新] 新闻动态
│   │   ├── Contact/             # [新] 联系我们
│   │   ├── WorkHome/            # [改] 样式商务化
│   │   ├── TeamHome/            # [改] 文案"部门化"
│   │   ├── UserHome/            # [未动]
│   │   ├── Information/         # [改] 文案调整
│   │   ├── Resume/              # [未动]
│   │   └── ChatHome/            # [未动]
│   └── router/index.js          # [改] 新增4条路由
├── screenshots/                  # 改造效果截图
└── ... 其余文件未改动
```

## 运行方式
```bash
cd xyzp5.3
npm install
npm run serve     # 开发模式 → http://localhost:8080
npm run build     # 生产构建 → dist/
```

*注意：前端通过 `/api` 代理到后端 `http://localhost:8081`，需后端服务配合才能完整运行（登录、职位列表等数据驱动功能）。静态页面（关于/产品/新闻/联系我们）可独立展示。*
