# 云笺 IM · 代码层级与文件职责速查

配套 `docs/ARCHITECTURE.md`。这份是**照当前项目实际结构**写的词典，回答「这个目录/文件是干什么的」。

标记说明：`[有内容]` 已有实际代码 · `[已清空]` 实现被移除、只剩壳 · `[空壳]` 只有引导注释、待实现 · `[空]` 空目录 · `[空文件]` 文件存在但没内容 · `[将来]` 蓝本规划、目前还没建 · `[待调整]` 位置与蓝本不一致

---

## 一、当前完整目录树（逐行注释）

```
web/
├── .workbuddy/                  # 项目工作数据与记忆笔记（不是缓存，不要删）
│   └── memory/                  #   MEMORY.md 长期约定 + 按日期的工作日志
├── docs/                        # 项目文档
│   ├── ARCHITECTURE.md          #   架构蓝本 v1.0：技术栈、阶段门禁、代码模板
│   ├── CODE_TREE.md             #   本文件：目录与文件职责速查
│   └── reference/
│       └── App.vue.bak          #   旧注册页实现备份（想回退时才打开）
├── node_modules/                # 依赖实物，npm 自动生成（不看、不改、不提交）
├── scripts/
│   └── scaffold.sh              # 按阶段建空目录，不装依赖、不覆盖已有文件
├── src/                         # 你写的代码全部在这里
│   ├── api/                     # [空] 所有网络请求，完整 URL 只允许出现在这里
│   ├── chat/                    # [空][待调整] IM 业务零件，蓝本里应在 components/chat/
│   ├── components/              # [空] 可复用组件总目录，蓝本里应包含 chat/ 与 ui/
│   ├── public/                  # [空][待调整] 静态资源，蓝本里应在项目根目录
│   ├── router/                  # [空] 路由表：哪个地址显示哪个页面
│   ├── stores/                  # [空] 跨页面共享的数据（登录态、消息列表）
│   ├── ui/                      # [空][待调整] shadcn 生成控件，蓝本里应在 components/ui/
│   ├── views/                   # 整页组件：注册页 / 登录页 / 聊天页
│   │   └── RegisterView.vue     #   [空壳] 注册页，待你自己按步骤实现
│   ├── App.vue                  # [已清空] 最外层壳，只渲染 RegisterView
│   ├── main.js                  # [有内容] 启动入口：createApp(App).mount('#app')
│   └── style.css                # [空文件] 全局样式入口，0 字节，尚未接线
├── .gitignore                   # [有内容] 忽略 node_modules/、dist/、*.local
├── index.html                   # [有内容] 网页骨架：div#app + 引入 main.js
├── package-lock.json            # [有内容] 依赖版本锁文件，别手改
├── package.json                 # [有内容] 依赖清单 + npm 脚本（dev/build/preview）
└── vite.config.js               # [有内容] Vite 配置：vue 插件，注释预留了后端代理
```

---

## 二、根级文件逐个说明

| 路径 | 干什么 | 什么时候动它 | Java 类比 |
|---|---|---|---|
| `index.html` | 浏览器真正打开的那张网页。里面只有一个空的 `<div id="app">` 和一行引入 `main.js` 的脚本；Vue 会把整个界面渲染进这个 div | 几乎不动。改标题、加 favicon、加 meta 时才动 | `web.xml` / 应用入口 |
| `src/main.js` | 启动入口。创建 Vue 应用实例并挂到 `#app` 上 | 每进入一个新阶段加一行（引入 CSS、router、Pinia） | `YunJianApplication.main()` |
| `src/App.vue` | 最外层壳。目前只做一件事：import 并渲染 `views/RegisterView.vue`；原来的注册表单实现已清空 | S2 接路由时改成 `<RouterView />` | 空的 ApplicationContext |
| `src/views/RegisterView.vue` | 注册页本体，当前是只有引导注释的空壳，等你按步骤实现 | 现在就在写它 | 一个 `@Controller` 方法对应的视图 |
| `src/style.css` | 全局样式与 Tailwind 入口。目前 **0 字节**，而且 `main.js` 没有 import 它，所以现在写任何样式都不会生效 | S1：写入 `@import "tailwindcss";`，并在 `main.js` 顶部加 `import './style.css'` | 全局配置文件 |
| `vite.config.js` | 构建工具配置。现在只注册了 vue 插件；注释里预留了联调时的 `server.proxy`（解决跨域） | S1 加 tailwind 插件和 `@` 别名；S3 加后端代理 | `pom.xml` |
| `package.json` | 依赖清单 + 三条命令：`npm run dev`（开发）、`build`（打包）、`preview`（预览打包结果） | 每进入一个阶段才 `npm install` 一次 | `pom.xml` 的 dependencies |
| `package-lock.json` | 锁住每个依赖的精确版本，保证换台机器装出来一模一样 | **永远不要手改**，npm 自动维护 | Maven 的版本锁定 |
| `.gitignore` | 告诉 Git 哪些不提交：`node_modules/`、`dist/`、`*.local` | S3 起追加 `.env.development` | `.gitignore` 同名同义 |
| `node_modules/` | 依赖的实体文件，由 npm 生成 | 不看、不改、不提交；出问题就删掉重新 `npm install` | 本地 Maven 仓库 |
| `.workbuddy/` | 本工作区的项目数据与记忆笔记，**不是缓存** | 不要删除 | — |
| `docs/` | 项目文档：`ARCHITECTURE.md`（规则）+ 本文件（词典）+ `reference/`（旧代码备份） | 架构有变更时才改，改完版本号 +1 | 项目 wiki |
| `scripts/` | 工具脚本 | 每次进入新阶段跑一次 `scaffold.sh` | 构建辅助脚本 |

---

## 三、`src/` 下各目录：将来各自放什么

> 除 `views/`（现有一个待实现的 `RegisterView.vue` 空壳）外，这些目录现在**都是空的**，也不含 `.gitkeep`。空目录不会被 Git 跟踪，换台机器克隆后就消失了，所以「先建好」并不等于「保存住了」。按蓝本它们本该在 S2~S4 才出现。

| 目录 | 职责（一句话） | 将来会出现的典型文件 | 对应阶段 |
|---|---|---|---|
| `src/api/` | 网络请求唯一出口，所有 `fetch` / `WebSocket` 都写这里 | `http.js` fetch 封装 · `auth.js` 注册/登录 · `message.js` 消息收发 · `socket.js` WebSocket | S3 |
| `src/router/` | 地址与页面的对应表 | `index.js` 路由表与懒加载配置 | S2 |
| `src/views/` | 一整个页面，负责「把零件拼起来 + 从 store 取数」 | `RegisterView.vue`（已有空壳） · `LoginView.vue` · `ChatView.vue` | S2 |
| `src/stores/` | 多个页面共用的一份数据 | `user.js` 登录态 · `chat.js` 消息列表 | S4 |
| `src/components/` | 可复用界面零件的总目录（本身不放文件，只放子目录） | 子目录 `chat/` 与 `ui/` | S2 / S5 |
| `src/chat/` | 当前平级目录，等价于蓝本的 `components/chat/`：IM 专属零件 | `MessageBubble.vue` 消息气泡 · `ConversationItem.vue` 会话项 · `MessageInput.vue` 输入框 | S2 |
| `src/ui/` | 当前平级目录，等价于蓝本的 `components/ui/`：shadcn-vue CLI 生成的通用控件 | `button/` · `dialog/` · `input/`（由 `npx shadcn-vue@latest add` 生成，不要手改文件名） | S5 |
| `src/public/` | 当前平级目录，本意是放静态资源 | `favicon.ico` · 默认头像 · 图标 | 用到图片时 |

### 还没建、蓝本里规划了的目录

| 目录 | 干什么 | 什么时候出现 |
|---|---|---|
| `public/`（**项目根**） | Vite 官方静态资源目录，内容会原样复制到打包结果 `dist/` 根目录，页面里用 `/logo.png` 直接引用 | S1 或第一次放图片时 |
| `src/lib/` | 无副作用的纯函数；`utils.js` 由 shadcn-vue 的 `init` 自动生成，内含 `cn()` 类名合并函数 | S5 |
| `src/composables/` | 抽出来的可复用响应式逻辑，例如 `useWebSocket.js` | S4 起，可选 |

---

## 四、当前结构与架构蓝本的 3 处出入

| # | 现在 | 蓝本约定 | 实际影响 | 建议 |
|---|---|---|---|---|
| 1 | `src/public/` | 根目录 `public/` | **这是个真实的坑**：Vite 只把项目根的 `public/` 当作静态资源目录，里面的文件才会复制进 `dist/`。放在 `src/public/` 里的图片不会被自动打包，用 `/xxx.png` 引用会 404 | 把 `src/public/` 移到项目根 `public/` |
| 2 | `src/chat/`、`src/ui/` 与 `src/components/` 平级 | `src/components/chat/`、`src/components/ui/` | 不影响运行，但破坏了「业务零件 / 生成控件都归 components 管」这条约定；`components.json` 里 `aliases.ui` 默认指向 `@/components/ui`，将来 shadcn CLI 会自己另建一份，出现两个 ui 目录 | 把两者移进 `components/`，删掉 `src/components/` 之外那份空壳 |
| 3 | 7 个空目录 + 1 个空壳页面 + 0 字节 `style.css` | 按 S1~S5 逐个创建，S1 需装 Tailwind 并接线 | 空目录不进 Git；`style.css` 没被 `main.js` 引入且 Tailwind 未安装，现在写样式不会生效；`package.json` 仍是 3 个依赖，说明 S1 尚未真正开始 | 要么保持空着先不管，要么按 S1 一次性接完 Tailwind |

> 以上 3 处**结构问题未做调整**，只是说明和建议；本次改动仅限清空注册页实现与同步文档。

---

## 五、一句话速记

```
index.html  网页壳
main.js     启动开关
App.vue     最外层壳（现在只渲染 RegisterView，以后只放 RouterView）
style.css   全局样式（Tailwind 入口）
vite.config.js / package.json / package-lock.json   构建三件套，少动

views/       整页 ← 注册页就在这里写
components/  零件（chat 业务件、ui 生成件）
api/         收发数据（唯一能写 URL 的地方）
stores/      共享数据
router/      地址映射
public/      图片（必须在项目根）
docs/        规则与词典
scripts/     建目录工具
```

数据流：**后端 → `api/` → `stores/` → `views/` → `components/`**，发送时反向。这条链路决定了为什么要有这么多目录。
