# 云笺 IM 前端 · 架构蓝本

版本 v1.0 · 本文件是项目唯一架构标准。

> **怎么用这份文件**：以后每次动手开发，先做三件事 —— ① 判断自己卡在哪，查第 4 节找到阶段编号；② 跑一次 `bash scripts/scaffold.sh <阶段号>` 建目录；③ 按第 5 节模板创建文件、按第 7 节验收。
> 平时只看第 0 节速查表就够了，卡住了再翻对应章节。
>
> **本文件定死的是**：技术选型、目录职责、依赖方向、命名、代码模板、引入顺序。
> **仍然按需的是**：依赖什么时候装、文件什么时候真的落盘。—— 这就是「用到才引入」的落地方式：规则和模板先定死，实现不提前做。

---

## 0. 一页速查

### 技术栈（定版，不再每次讨论）

| 层 | 选型 | 阶段 | 安装命令 | Java 类比 |
|---|---|---|---|---|
| 构建 | Vite 8 + @vitejs/plugin-vue | 已有 | — | Maven / Gradle |
| 框架 | Vue 3（SFC + `<script setup>`） | 已有 | — | Spring 容器 |
| 语言 | **JavaScript，不用 TypeScript** | 已有 | — | 用 Java，不引入 Kotlin |
| 样式 | Tailwind CSS **v4** + @tailwindcss/vite | S1 | `npm i tailwindcss @tailwindcss/vite` | 注解代替 XML 配置 |
| 组件 | **shadcn-vue**（CLI 按需生成源码） | S5 | `npx shadcn-vue@latest add <组件>` | 可改源码的公共库 |
| 路由 | vue-router 4 | S2 | `npm i vue-router@4` | @RequestMapping 映射表 |
| 状态 | Pinia | S4 | `npm i pinia` | 单例 @Service |
| 网络 | 原生 `fetch` / `WebSocket`，**不用 axios** | S3 | 无需安装 | RestTemplate |
| 图标 | lucide-vue-next（随 shadcn-vue 带入） | S5 | 无需单独安装 | — |
| 包管理 | npm（已有 package-lock.json） | 已有 | — | Maven |

### 当前状态

**S0 已完成**。项目里只有 Vite 8 + Vue 3 + 一个注册页空壳（`src/views/RegisterView.vue`，待实现），`src/App.vue` 只负责渲染它。没有 Tailwind、没有路由、没有状态管理、没有组件库。`scripts/` 和 `docs/` 是本蓝本自带的工具与说明，不属于业务代码；`docs/reference/App.vue.bak` 是旧版注册页实现的备份。

### 一句话记住分层

```
Vite 跑起来 → Vue 3 拆组件 → Tailwind 管长相 → shadcn 提供现成零件 → 你写聊天界面
数据流：后端 WebSocket → api/ → stores/ → views/ → components/   （发送时反向）
```

---

## 1. 技术栈定版说明

### 1.1 语言：JavaScript，不引入 TypeScript

- **理由**：你前端基础还在建立期，TS 的类型标注、泛型、配置文件会同时增加三份认知负担。先把「组件 / props / 响应式」这三件事跑通更重要。
- **代价**：没有编译期类型检查，字段名写错要等运行时才发现。
- **如果以后要改**：单独立项做迁移，不要在写业务的中途顺手加 TS。

### 1.2 shadcn-vue 初始化时不可改的三项（现在提前定死）

`npx shadcn-vue@latest init` 会问几个问题，其中三项**初始化后无法修改**，所以在这里定好，S5 时照抄：

| 配置项 | 定版值 | 理由 |
|---|---|---|
| `style` | `new-york` | 视觉更紧凑，社区示例多 |
| `tailwind.baseColor` | `neutral` | 中性灰不抢主题色，便于后续换品牌色 |
| `tailwind.cssVariables` | `true` | 深色模式必需 |
| `typescript` | `false` | 生成 JS 版 `.vue` 组件，配合 1.1 |
| `iconLibrary` | `lucide` | 官方默认，随组件带入 |
| `tailwind.config` | 留空 | Tailwind v4 不需要配置文件 |

### 1.3 明确不引入的东西

`axios`、`TypeScript`、`Element Plus / Naive UI 等整包组件库`、`Sass / Less`、`postcss.config.js`、`tailwind.config.js`、`add --all`（一次性装全部 shadcn 组件）、任何 CSS-in-JS 方案。

**判断新依赖能不能加，只问两句**：它属于第 4 节哪个阶段？有没有浏览器原生的替代？答不上来就先别装。

---

## 2. 目录契约

```
web/
├── index.html                    网页入口（已有）
├── vite.config.js                Vite 配置：插件 + @ 别名 + 后端代理（已有）
├── jsconfig.json                 让编辑器和 shadcn CLI 认识 @ 别名（S1）
├── components.json               shadcn-vue 配置（S5，由 init 生成，勿手写）
├── package.json                  依赖清单（已有）
├── .env.example                  环境变量样例，进 Git（S3）
├── .env.development              本地后端地址，不进 Git（S3）
├── docs/
│   └── ARCHITECTURE.md           本文件
├── scripts/
│   └── scaffold.sh               目录骨架脚本（只建目录，不装依赖）
├── public/                       图标、默认头像（S1 起按需）
└── src/
    ├── main.js                    启动入口（已有）
    ├── App.vue                    根组件（已有）
    ├── style.css                  Tailwind 入口 + 主题 token（S1）
    ├── router/
    │   └── index.js               路由表（S2）
    ├── views/                     整页组件（S2）
    │   ├── RegisterView.vue
    │   ├── LoginView.vue
    │   └── ChatView.vue
    ├── components/
    │   ├── ui/                    shadcn-vue 生成的零件（S5，勿改文件名）
    │   └── chat/                  业务零件（S2 起）
    │       ├── MessageBubble.vue
    │       ├── ConversationItem.vue
    │       └── MessageInput.vue
    ├── api/                       网络请求唯一出口（S3）
    │   ├── http.js                fetch 封装
    │   ├── auth.js
    │   └── message.js
    ├── stores/                    跨页面共享状态（S4）
    │   ├── user.js
    │   └── chat.js
    ├── composables/               可复用逻辑（S4 起，可选）
    │   └── useWebSocket.js
    └── lib/
        └── utils.js               cn() 等纯函数（S5 由 CLI 生成，勿手写）
```

### 每个目录的职责边界

| 目录 | 放什么 | 禁止放什么 | 创建时机 |
|---|---|---|---|
| `src/views/` | 一整个页面，负责「把零件拼起来 + 从 store 取数」 | 可复用的 UI 零件、直接的 fetch 调用 | S2 |
| `src/components/chat/` | IM 专属零件（气泡、会话项、输入框） | 网络请求、跨页面状态 | S2 起 |
| `src/components/ui/` | shadcn-vue CLI 生成的通用控件 | 业务逻辑、任何 import `api/` / `stores/` | S5 |
| `src/api/` | 所有 `fetch` / `WebSocket`，URL 只在这里出现 | 界面代码、组件引用 | S3 |
| `src/stores/` | 多个页面要共用的一份数据 | 临时表单数据、单个组件内部状态 | S4 |
| `src/composables/` | 抽出来的可复用响应式逻辑 | 全局单例数据（那属于 stores） | S4 起，可选 |
| `src/lib/` | 无副作用的纯函数 | 任何依赖 Vue 组件的代码 | S5（CLI 生成） |
| `src/router/` | 路由表与守卫 | 数据请求、业务判断 | S2 |
| `public/` | 图片、图标、favicon | 需要被打包处理的源码 | 用到图片时 |

> **Java 类比**：这跟后端分包是一个道理。`controller` 不放 SQL，`mapper` 不返回页面。目录职责定死，以后找代码、改代码才不用靠记忆。

---

## 3. 分层依赖规则（单向，禁止成环）

```
main.js
  └→ App.vue
      └→ router/          → views/
          └→ views/       → components/chat/  → components/ui/  → lib/
                          → api/             → lib/
                          → stores/          → api/  → lib/
```

| 层 | 允许 import | 禁止 import |
|---|---|---|
| `lib/` | 无（最底层，纯函数） | 项目内任何其他层 |
| `api/` | `lib/` | `stores/` `views/` `components/` `router/` |
| `stores/` | `api/` `lib/` | `views/` `components/` `router/` |
| `components/ui/` | `lib/utils` 与同目录组件 | `api/` `stores/` `views/` `router/` |
| `components/chat/` | `components/ui/` `lib/` `stores/`（只读） | `api/` `views/` `router/` |
| `router/` | `views/` | `api/` `stores/` |
| `views/` | 以上全部 | 无（最上层） |

### 四条铁律

1. **所有 `fetch` / `WebSocket` 只能写在 `api/`**。组件里出现完整 URL 即为违规。
2. **`components/ui/` 是 CLI 生成的**：只改样式，不塞业务逻辑，不改文件名（否则 CLI 更新会覆盖）。
3. **禁止循环依赖**。发现 A 和 B 互相 import，说明公共部分该下沉到 `lib/` 或 `stores/`。
4. **跨页面共享的数据必须进 `stores/`**，不许挂在 `window` 上，也不许用模块级全局变量凑合。

---

## 4. 阶段门禁（核心）

> 规则：**卡住了才进下一阶段**。没触发条件，就不许提前装依赖、提前建目录。

### S0 · 现状（已完成）

- 内容：Vite 8 + Vue 3 + 注册页空壳（`src/views/RegisterView.vue`，待实现）；旧版注册表单实现已备份到 `docs/reference/App.vue.bak`，`src/App.vue` 现只负责渲染该页面
- 完成判据：`npm run dev` 能打开注册页
- 禁止：装 Tailwind、建空目录、装组件库

### S1 · 样式与路径别名

- **触发**：要写第二个页面，或觉得手写 CSS 重复啰嗦
- 命令：
  ```bash
  npm install tailwindcss @tailwindcss/vite
  bash scripts/scaffold.sh 1
  ```
- 新增/修改：`src/style.css`、`jsconfig.json`、`vite.config.js`（加插件 + `@` 别名）、`main.js`（加一行 CSS import）
- **Tailwind v4 注意**：不要建 `tailwind.config.js`，不要建 `postcss.config.js`，不要写 content 数组，`style.css` 里就是一行 `@import "tailwindcss";`
- 完成判据：写 `<div class="p-4 bg-red-500">` 能看到红色内边距；`npm run build` 通过
- 禁止：引入 shadcn、装路由

### S2 · 页面与路由

- **触发**：需要两个以上页面互相跳转
- 命令：`npm install vue-router@4` + `bash scripts/scaffold.sh 2`
- 新增：`router/index.js`、`views/*.vue`、`components/chat/*.vue`
- 完成判据：地址栏直接输入 `/chat` 能打开，刷新不白屏
- 禁止：连后端、装 Pinia

### S3 · 后端联调

- **触发**：要把注册表单 POST 给后端（`App.vue` 注释里已写好 `/api/v1/auth/register`）
- 命令：`bash scripts/scaffold.sh 3`（**不需要装任何依赖**，用原生 fetch）
- 新增/修改：`vite.config.js` 加 `server.proxy`、`.env.example`、`.env.development`、`api/http.js`、`api/auth.js`
- 完成判据：注册请求在浏览器 Network 里出现在 `/api/...`，后端能收到
- 禁止：把后端地址写死在组件里

### S4 · 共享状态

- **触发**：登录态或消息列表要在多个页面同时用
- 命令：`npm install pinia` + `bash scripts/scaffold.sh 4`，`main.js` 里加 `app.use(createPinia())`
- 新增：`stores/user.js`、`stores/chat.js`
- 完成判据：切换页面后再回来，数据不丢
- 禁止：用组件变量或 `window` 跨页面传数据

### S5 · 组件库

- **触发**：按钮 / 弹窗 / 输入框手写重复，或需要键盘可达、焦点管理等无障碍能力
- **S5a 一次性初始化**（前置：S1 的 `jsconfig.json`、`@` 别名、`style.css` 只有一行 import 都必须就绪）
  ```bash
  npx shadcn-vue@latest init
  ```
  选项一律按 1.2 定版值填。此命令会生成 `components.json`、`src/lib/utils.js` 并装入 `clsx`、`tailwind-merge`、`lucide-vue-next`。
- **S5b 每次只加一个组件**
  ```bash
  npx shadcn-vue@latest add button
  ```
- 完成判据：页面里 `import { Button } from '@/components/ui/button'` 能渲染
- 禁止：`add --all`、手改生成文件名、把业务请求写进 `ui/` 组件

> **顺序说明**：S3 与 S4 可互换（哪个先卡住先做哪个）；**S5 必须在 S1 之后**，否则 CLI 的前置校验会失败。

---

## 5. 标准代码模板

> 全部为 JavaScript。复制后按实际业务改，但**结构不要改**。

### 5.1 `vite.config.js`（S3 后的最终形态，每阶段只加自己那部分）

```js
import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())

  return {
    plugins: [vue(), tailwindcss()],
    resolve: {
      alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) },
    },
    server: {
      proxy: {
        '/api': { target: env.VITE_API_TARGET, changeOrigin: true },
      },
    },
  }
})
```

### 5.2 `jsconfig.json`（S1）

```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": { "@/*": ["./src/*"] },
    "checkJs": false
  },
  "include": ["src/**/*", "vite.config.js"],
  "exclude": ["node_modules", "dist"]
}
```

> 没有这个文件，编辑器不认识 `@/`，shadcn-vue CLI 的校验也会报错。项目不用 TS，所以是 `jsconfig.json` 而不是 `tsconfig.json`。

### 5.3 `src/style.css`（S1）

```css
@import "tailwindcss";
@custom-variant dark (&:where(.dark, .dark *));

@theme {
  --color-brand: #2dd4bf;
}
```

> `--color-brand` 会自动生成 `bg-brand` / `text-brand` 等类，沿用你现在注册页的主色。以后换主题只改这里，不改组件。

### 5.4 `src/main.js`（S4 后的最终形态，每阶段只加一行）

```js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './style.css'

createApp(App).use(createPinia()).use(router).mount('#app')
```

### 5.5 `src/router/index.js`（S2）

```js
import { createRouter, createWebHistory } from 'vue-router'

// 类比 @RequestMapping 映射表；() => import() 懒加载，首屏不必下载所有页面
export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/register' },
    { path: '/register', component: () => import('@/views/RegisterView.vue') },
    { path: '/login', component: () => import('@/views/LoginView.vue') },
    { path: '/chat', component: () => import('@/views/ChatView.vue') },
  ],
})
```

> 用 `createWebHistory` 部署时后端需要配置「找不到路由就回退到 index.html」，开发阶段 Vite 已自动处理。

### 5.6 `src/views/ChatView.vue`（S2，整页）

```vue
<script setup>
// 整页：只负责把零件拼起来 + 从 store 取数
// 类比 @Controller：接请求、调 Service、返回视图，不写 SQL
import { computed } from 'vue'
import { useChatStore } from '@/stores/chat'
import MessageBubble from '@/components/chat/MessageBubble.vue'

const chatStore = useChatStore()
const messages = computed(() => chatStore.messages)
</script>

<template>
  <section class="flex h-screen flex-col gap-2 p-4">
    <MessageBubble
      v-for="msg in messages"
      :key="msg.id"
      :content="msg.content"
      :mine="msg.mine"
    />
  </section>
</template>
```

### 5.7 `src/components/chat/MessageBubble.vue`（S2，业务零件）

```vue
<script setup>
// props ≈ 方法入参；emit ≈ 回调接口
// 零件只管显示和往外抛事件，绝不自己发请求
defineProps({
  content: { type: String, required: true },
  mine: { type: Boolean, default: false },
})
const emit = defineEmits(['retry'])
</script>

<template>
  <div
    :class="[
      'rounded-lg px-3 py-2',
      mine ? 'bg-teal-50 dark:bg-teal-950' : 'bg-gray-100 dark:bg-gray-800',
    ]"
  >
    {{ content }}
  </div>
</template>
```

### 5.8 `src/api/http.js`（S3，网络唯一出口）

```js
// 类比后端里配置好的 RestTemplate / Feign Client
const BASE = '/api'

export async function request(path, { method = 'GET', body, signal } = {}) {
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: body ? { 'Content-Type': 'application/json' } : undefined,
    body: body ? JSON.stringify(body) : undefined,
    signal,
  })

  if (!res.ok) {
    const detail = await res.text().catch(() => '')
    throw new Error(`请求失败 ${res.status}：${detail || res.statusText}`)
  }

  return res.status === 204 ? null : res.json()
}
```

### 5.9 `src/api/auth.js`（S3）

```js
import { request } from './http'

// 字段名必须与后端 UserRegisterDTO 完全一致
export function register(payload) {
  return request('/v1/auth/register', { method: 'POST', body: payload })
}
```

### 5.10 `src/stores/chat.js`（S4，Pinia setup 写法）

```js
import { ref } from 'vue'
import { defineStore } from 'pinia'
import * as messageApi from '@/api/message'

// 类比 Spring 的单例 @Service：全应用一份数据，多个页面读同一份
export const useChatStore = defineStore('chat', () => {
  const messages = ref([])
  const loading = ref(false)
  const error = ref('')

  async function loadHistory(conversationId) {
    loading.value = true
    error.value = ''
    try {
      messages.value = await messageApi.listMessages(conversationId)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  function appendMessage(msg) {
    messages.value.push(msg)
  }

  return { messages, loading, error, loadHistory, appendMessage }
})
```

> `loading` / `error` / 空数组分别对应界面的加载态、错误态、空态，写页面时三种都要处理。

### 5.11 `.env.example`（S3）

```
VITE_API_TARGET=http://localhost:8080
```

> 复制一份为 `.env.development` 填本机后端地址，后端地址只在这里改。只有 `VITE_` 前缀的变量才会被 Vite 处理。**不要把 `.env.development` 提交到 Git**。

### 5.12 `components.json`（S5，供核对，不要手写）

```json
{
  "$schema": "https://shadcn-vue.com/schema.json",
  "style": "new-york",
  "typescript": false,
  "tailwind": {
    "config": "",
    "css": "src/style.css",
    "baseColor": "neutral",
    "cssVariables": true,
    "prefix": ""
  },
  "aliases": {
    "components": "@/components",
    "composables": "@/composables",
    "utils": "@/lib/utils",
    "ui": "@/components/ui",
    "lib": "@/lib"
  },
  "iconLibrary": "lucide"
}
```

---

## 6. 命名规范

| 对象 | 规则 | 示例 |
|---|---|---|
| 目录 | 小写、复数、不加缩写 | `views/` `components/` `stores/` `api/` |
| 整页文件 | PascalCase + `View` 后缀 | `ChatView.vue` |
| 业务组件 | PascalCase，名词 | `MessageBubble.vue` |
| shadcn 生成组件 | 保持 CLI 原名，**不改** | `components/ui/button/` |
| api 模块 | 小驼峰，按业务域命名 | `auth.js` `message.js` |
| api 函数 | 动词开头 | `register()` `listMessages()` `sendMessage()` |
| store | `useXxxStore`，文件小驼峰 | `useChatStore`，`stores/chat.js` |
| 常量 | 全大写下划线 | `BASE`、`MAX_RETRY` |
| 组件事件 | kebab-case | `@retry-send` |
| 主题 token | `--color-xxx` | `--color-brand` |

---

## 7. 每阶段自检清单

每完成一个阶段，逐条打勾再进入下一阶段：

- [ ] `npm run dev` 能启动，页面正常显示
- [ ] `npm run build` 通过，无报错
- [ ] 新增文件都在第 2 节规定的目录里，没有自创目录
- [ ] 没有出现第 3 节禁止的反向 import
- [ ] 所有网络请求都在 `api/`，组件里没有完整 URL
- [ ] 本阶段该装的依赖装了，**下一阶段的依赖一个都没装**
- [ ] 页面直接输入地址访问、刷新都不白屏（S2 起）
- [ ] 加载中 / 空数据 / 出错 / 正常 四种状态都有对应显示（S4 起）

---

## 8. 决策记录（替你拍板的部分，附代价）

| 编号 | 决定 | 理由 | 代价 / 想改的话 |
|---|---|---|---|
| D1 | **Tailwind v4**（不是 v3） | shadcn-vue 当前版本官方面向 v4；v4 无需配置文件、无需 PostCSS、自动扫描模板，对新手反而更简单 | 需要较新浏览器（近 3 年内）。**这一条修正了上一轮我给的「v3 起步」建议**，原因是核实了 shadcn-vue 官方文档：v3 需锁 `shadcn-vue@1.0.3` 旧版 |
| D2 | **shadcn-vue**（不是 React 原版 shadcn/ui） | 原版是 React 生态，Vue 3 用社区移植版，CLI 按需生成组件源码的机制完全一致 | 组件数量和更新节奏略慢于 React 原版 |
| D3 | 纯 **JavaScript**，不用 TypeScript | 先降低认知负担 | 无编译期类型检查；以后想加需单独立项 |
| D4 | 网络层用原生 `fetch`，不装 axios | 少一个依赖，封装一次即可复用 | 拦截器、超时等要自己写（模板 5.8 已覆盖基础） |
| D5 | 图标随 shadcn-vue 带入 `lucide-vue-next`，不单独选型 | 避免多套图标风格 | 若以后要换，统一改 `components.json` |
| D6 | 深色模式用 CSS 变量 + `.dark` class | shadcn-vue 要求 `cssVariables: true` | 主题色必须写进 `style.css` 的 `@theme`，不能在组件里写死色值 |
| D7 | **不提前建空目录、不提前装依赖** | 保留你要的「用到才引入」 | 每次新阶段多跑一次 `scaffold.sh`，换来的是任何时候都看得懂项目 |

---

## 9. 变更规则

1. 要改这套标准，必须显式修改本文件、版本号 +1，并在对话里说清改动影响哪几个阶段。
2. 引入任何新第三方依赖前，先回答：属于哪个阶段？有没有原生替代？答不上来就不装。
3. 如果某次改动影响了已完成阶段的代码（例如换主题色、改 `vite.config.js`），要按第 7 节重新验收受影响阶段，不能只在心里带过。
4. 遇到本文件没覆盖的情况：先提出问题和两个候选方案，选定了再补齐到本文件，不要临场拍脑袋发明一个新目录。
