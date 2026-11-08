# 云笺（YunJian）IM

[![CI](https://github.com/qiujunjie1101-dotcom/yunjian/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/qiujunjie1101-dotcom/yunjian/actions/workflows/ci.yml)

基于 Spring Boot 3 + Vue 3 的即时通讯（IM）项目，采用前后端分离架构：`server/` 为 Spring Boot 服务端，对外提供 RESTful 接口与统一响应体；`web/` 为 Vue 3 前端，负责用户交互。两者同属一个仓库（monorepo），共用同一套 CI 与文档。

本项目服务端参照《阿里巴巴 Java 开发手册》（嵩山版）的**命名规约、异常与日志规约、应用分层、MySQL 数据库规约**进行工程组织，所有新增后端代码须符合手册中的【强制】项。

---

## 1. 项目简介

云笺 IM 面向单聊与群聊场景，服务端承担用户账号、会话、消息、在线状态等核心能力。

本仓库按「前后端分离、单仓双目录」组织：`server/` 是 Maven 主工程（artifactId 仍为 `yunjian-server`），`web/` 是 Vite 前端工程。后续如需新增网关、客户端等模块，直接在仓库根下平级新增目录即可，无需另建仓库。

**当前进度**

| 能力 | 状态 |
|---|---|
| 工程骨架、依赖管理 | ✅ 已完成 |
| 统一响应体、错误码体系 | ✅ 已完成 |
| 全局异常处理 | ✅ 已完成 |
| 健康检查接口 | ✅ 已完成 |
| CI 构建流水线 | ✅ 已完成 |
| 用户注册接口（BCrypt 加密） | ✅ 已完成 |
| 用户登录 / JWT 鉴权（Redis 会话白名单） | ✅ 已完成 |
| 用户登出 / 当前用户资料查询 | ✅ 已完成 |
| 开发与生产配置分离（profile） | ✅ 已完成 |
| 前端工程骨架（Vue 3 + Vite） | ✅ 已完成 |
| 前端注册页面与接口联调 | 🚧 开发中 |
| 前端登录页面 | 🚧 规划中 |
| WebSocket 长连接、消息收发 | 🚧 规划中 |
| 会话与消息持久化 | 🚧 规划中 |

---

## 2. 技术栈

| 组件 | 版本 | 用途 |
|---|---|---|
| JDK | 17 | 运行时（CI 使用 Temurin 17） |
| Spring Boot | 3.2.4 | 基础框架 |
| Spring Web | 随 Boot 托管 | RESTful 接口 |
| Spring Validation | 随 Boot 托管 | 参数校验（`@Validated`） |
| MyBatis-Plus | 3.5.7 | 持久层增强 |
| MySQL Connector/J | 随 Boot 托管 | MySQL 驱动 |
| Hutool | 5.8.27 | 通用工具集 |
| Lombok | 随 Boot 托管 | 简化 POJO |
| spring-security-crypto | 随 Boot 托管 | 密码加密（BCrypt），不引入完整 Security 框架 |
| Spring Data Redis | 随 Boot 托管 | 登录会话白名单，按 jti 维度存储 |
| JJWT | 0.11.5 | JWT 签发与校验（HS256） |
| Maven | 3.9+ | 构建（`server/`） |
| Node.js | 22 LTS | 前端运行时与包管理（`web/`） |
| Vue | 3.5.x | UI 框架（`web/`） |
| Vite | 8.x | 开发服务器与构建工具（`web/`） |
| @vitejs/plugin-vue | 6.x | 编译 `.vue` 单文件组件（`web/`） |

---

## 3. 工程结构

遵循手册「工程结构 - 应用分层」规约：**Web 层只做参数校验与结果封装，业务逻辑下沉至 Service 层，数据库访问收敛在 DAO 层**，禁止跨层调用、禁止 Controller 直连 Mapper。

```
yujian                                   # 仓库根（monorepo）
├── .github/workflows/ci.yml             # GitHub Actions 构建流水线
├── .gitattributes                       # 换行符统一（LF）
├── .gitignore
├── README.md
├── server/                              # 后端：Spring Boot 3（Maven）
│   ├── pom.xml
│   └── src/main
│       ├── java/com/bond/yunjian
│       │   ├── YunJianApplication.java    # 启动类
│       │   ├── common                     # 通用能力
│       │   │   ├── constant               # 常量（鉴权请求头、Redis 键）
│       │   │   ├── context                # 登录用户上下文（ThreadLocal）
│       │   │   ├── exception              # 异常体系
│       │   │   └── result                 # 统一响应
│       │   ├── config                     # 框架配置（拦截器、密码编码器、字段填充）
│       │   ├── controller                 # Web 层
│       │   ├── dto                        # 入参传输对象
│       │   ├── entity                     # 数据库实体（DO）
│       │   ├── mapper                     # DAO 层
│       │   ├── service                    # Service 层
│       │   ├── utils                      # JWT 签发解析、令牌提取
│       │   └── vo                         # 出参展示对象
│       └── resources
│           ├── application.yml            # 公共配置，默认激活 dev
│           ├── application-dev.yml        # 开发：本机 MySQL/Redis，打印 SQL
│           └── application-prod.yml       # 生产：敏感项全部走环境变量
└── web/                                 # 前端：Vue 3 + Vite
    ├── index.html                       # 浏览器唯一入口
    ├── vite.config.js                   # 构建配置与开发代理
    ├── package.json
    └── src
        ├── main.js                      # 应用入口，挂载到 #app
        └── App.vue                      # 根组件
```

**规划中的分层**（新增业务时按此建包，包名全小写、单数）：

| 分层 | 包名 | 职责 | 命名后缀 |
|---|---|---|---|
| Web 层 | `controller` | 参数校验、调用 Service、返回 `Result` | `Controller` |
| Service 层 | `service` | 业务逻辑编排 | `Service` / `ServiceImpl` |
| DAO 层 | `mapper` | 数据库访问 | `Mapper` |
| 数据对象 | `entity` | 与数据库表一一对应 | `DO` |
| 传输对象 | `dto` | 层间传输 | `DTO` |
| 展示对象 | `vo` | 返回给前端 | `VO` |
| 配置 | `config` | 框架配置 | `Config` |

---

## 4. 环境要求

| 范围 | 要求 |
|---|---|
| 后端 `server/` | JDK 17+、Maven 3.9+、MySQL 8.0+、**Redis 5.0+**（登录会话依赖，未启动则登录和鉴权接口不可用） |
| 前端 `web/` | Node.js 22 LTS 及以上（含 npm 10+） |

---

## 5. 快速开始

### 5.1 初始化数据库

遵循手册 MySQL 规约：**库名与应用名一致、字符集统一 utf8mb4**。

```sql
CREATE DATABASE IF NOT EXISTS yunjian
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
```

### 5.2 配置说明（开发 / 生产分离）

配置按 profile 拆成三个文件，公共项与敏感项彻底分开：

| 文件 | 用途 | 内容 |
|---|---|---|
| `application.yml` | 公共 | 端口、`/api/v1` 前缀、MyBatis-Plus、JWT 有效期；默认激活 `dev` |
| `application-dev.yml` | 本地开发 | MySQL/Redis 连 `localhost`、控制台打印 SQL、固定开发密钥 |
| `application-prod.yml` | 生产部署 | 全部走环境变量，**缺失即启动失败**，不设默认值兜底 |

本机开发无需设置任何环境变量，直接启动即为 `dev`。生产部署时设置 `SPRING_PROFILES_ACTIVE=prod` 并注入以下变量：

| 变量 | 必需 | 说明 |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | 是 | 固定为 `prod` |
| `DB_URL` | 是 | JDBC 连接串 |
| `DB_USERNAME` / `DB_PASSWORD` | 是 | 数据库账号与密码 |
| `REDIS_HOST` | 是 | Redis 地址 |
| `REDIS_PORT` / `REDIS_PASSWORD` / `REDIS_DATABASE` | 否 | 默认 `6379` / 空 / `0` |
| `YUNJIAN_JWT_SECRET` | 是 | JWT 签名密钥，**长度不少于 32 字节**，可用 `openssl rand -base64 48` 生成 |

> ⚠️ `YUNJIAN_JWT_SECRET` 一旦泄露，任何人都能伪造任意用户令牌。该值不写进任何提交文件，只在部署环境注入。

生产启动示例（Linux 服务器）：

```bash
SPRING_PROFILES_ACTIVE=prod \
DB_URL='jdbc:mysql://127.0.0.1:3306/yunjian?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai' \
DB_USERNAME=yunjian DB_PASSWORD='***' \
REDIS_HOST=127.0.0.1 YUNJIAN_JWT_SECRET='***' \
java -jar yunjian-server-1.0-SNAPSHOT.jar
```


### 5.3 启动后端

```bash
# 所有 Maven 命令都在 server/ 目录下执行
cd server

# 方式一：直接运行
mvn spring-boot:run

# 方式二：打包后运行
mvn clean package -DskipTests
java -jar target/yunjian-server-1.0-SNAPSHOT.jar
```

服务默认监听 **8123** 端口，所有接口统一前缀 `/api/v1`（由 `server.servlet.context-path` 提供）。启动成功后可访问健康检查接口：

```bash
curl http://localhost:8123/api/v1/health
```

> 登录与鉴权依赖 Redis。本机已将 Redis 注册为 Windows 服务（服务名 `Redis`，启动类型自动），可用 `Get-Service Redis` 确认状态；未启动时登录会返回 `B0302` 缓存服务异常。

### 5.4 启动前端

```bash
# 所有 npm 命令都在 web/ 目录下执行
cd web
npm install
npm run dev
```

前端开发服务器默认监听 `http://localhost:5173`。前端与后端端口不同，联调时需在 `web/vite.config.js` 配置 `server.proxy`，把 `/api/v1` 转发到 `http://localhost:8123`（待接入）。生产构建使用 `npm run build`，产物输出到 `web/dist/`。

---

## 6. 接口规范

### 6.1 统一响应体

所有接口统一返回 `Result<T>`，结构固定为四个字段，前端只需判断 `success`：

```json
{
  "success": true,
  "code": "00000",
  "msg": "success",
  "data": ["张三", "李四", "王五"]
}
```

| 字段 | 类型 | 说明 |
|---|---|---|
| `success` | boolean | 是否成功，前端唯一判据 |
| `code` | String | 5 位错误码，成功恒为 `00000` |
| `msg` | String | 可直接展示给用户的提示信息 |
| `data` | T | 业务数据，失败时为 `null` |

### 6.2 错误码规范

错误码为 **5 位字符串**，格式为「来源字母 + 4 位数字」，来源划分为：

| 前缀 | 含义 | 示例 |
|---|---|---|
| `00000` | 成功 | `SUCCESS` |
| `A` | 用户端错误（参数、登录、权限） | `A0400` 请求参数不合法 |
| `B` | 当前系统错误（服务自身异常） | `B0001` 系统内部错误 |
| `C` | 第三方服务错误（外部依赖失败） | `C0001` 第三方服务调用失败 |

编码段位约定：`A04xx` 参数与校验类、`A02xx` 登录与认证类、`A03xx` 权限类、`B01xx` 系统繁忙与超时、`B03xx` 数据库与中间件。新增错误码必须在 `ResultCode` 枚举中定义，**禁止在代码中直接硬编码错误码字符串或魔法值**。

### 6.3 已有接口

所有接口统一带 `/api/v1` 前缀，下表为完整路径。

| 方法 | 路径 | 鉴权 | 说明 | 返回 |
|---|---|---|---|---|
| GET | `/api/v1/health` | 免 | 健康检查，用于部署探活 | `Result<String[]>` |
| POST | `/api/v1/auth/register` | 免 | 用户注册 | `Result<Void>` |
| POST | `/api/v1/auth/login` | 免 | 用户登录，返回访问令牌 | `Result<UserLoginVO>` |
| POST | `/api/v1/auth/logout` | 需 | 注销当前令牌对应的会话 | `Result<Void>` |
| GET | `/api/v1/user/info` | 需 | 当前登录用户资料 | `Result<UserInfoVO>` |
| GET | `/api/v1/test` | 免 | **临时调试端点**，联调完成后应移除 | `Result<String>` |

**鉴权机制**

登录成功后返回 JWT，后续请求放在请求头：`Authorization: Bearer <token>`。校验链路为「验签 → 查 Redis 会话白名单 → 写入当前用户上下文」，两道都通过才放行。

- 令牌本身有效期 7200 秒（`yunjian.jwt.expire-seconds`），Redis 会话键 `yunjian:login:token:{userId}:{jti}` 的 TTL 与之同源。
- 登出会把会话键删掉，**令牌即使还没过期也会立即失效**，这是白名单机制相比纯 JWT 的价值。
- 同一账号多端登录会生成多个 `jti`，各自独立有效；踢下线需额外实现，目前未做。
- 错误码：令牌过期或会话不存在返回 `A0201`，令牌非法/被篡改返回 `A0203`。


**用户注册**

请求体字段与校验规则：

| 字段 | 类型 | 必填 | 校验规则 |
|---|---|---|---|
| `account` | String | 是 | 4–20 位，仅允许字母、数字、下划线；全局唯一 |
| `username` | String | 是 | 2–20 位，昵称，允许重复 |
| `password` | String | 是 | 6–20 位，入库前以 BCrypt 加盐哈希，**不存明文** |

```bash
curl -X POST http://localhost:8123/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"account":"yunjian_01","username":"云笺用户","password":"123456"}'
```

成功响应（`data` 为空，仅返回状态）：

```json
{ "success": true, "code": "00000", "msg": "success", "data": null }
```

账号已存在时返回业务错误码 `A0406`（该用户已注册），由 `GlobalExceptionHandler` 统一转换为 `Result`，不会抛 500。

**用户登录**

```bash
curl -X POST http://localhost:8123/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"account":"yunjian_01","password":"123456"}'
```

成功响应（`data` 只含令牌信息，用户资料走 `/user/info` 单独获取）：

```json
{
  "success": true,
  "code": "00000",
  "msg": "success",
  "data": { "token": "eyJhbGciOi...", "tokenType": "Bearer", "expiresIn": 7200 }
}
```

账号不存在与密码错误返回同一错误码 `A0202`，不区分二者，避免账号被枚举探测。

**当前用户资料**

```bash
curl http://localhost:8123/api/v1/user/info \
  -H "Authorization: Bearer <token>"
```

返回 `id / account / username / avatar / signature / gender / lastLoginTime`。用户 ID 取自登录上下文，不接受外部传参，因此不存在水平越权修改他人资料的可能。


---

## 7. 编码规约

以下为《阿里巴巴 Java 开发手册》中本项目**强制执行**的条款，提交前自查：

**命名**

- 【强制】类名 `UpperCamelCase`，方法名、参数名、成员变量、局部变量 `lowerCamelCase`，常量 `UPPER_SNAKE_CASE`。
- 【强制】包名统一小写、单数形式，点分隔符之间仅有一个自然语义的英文单词。
- 【强制】抽象类以 `Abstract` 或 `Base` 开头，异常类以 `Exception` 结尾，测试类以 `Test` 结尾。

**常量与魔法值**

- 【强制】不允许任何魔法值（未经预先定义的常量）直接出现在代码中，错误码、状态值一律走枚举或常量类。

**对象与 POJO**

- 【强制】POJO 类属性必须使用包装数据类型，禁止使用基本类型（数据库返回值可能为 `null`）。
- 【强制】POJO 类布尔类型属性**不加 `is` 前缀**，否则部分框架解析会引起序列化错误。
- 【强制】禁止在 POJO 类中同时存在对应属性 `xxx` 的 `isXxx()` 和 `getXxx()` 方法。

**异常与日志**

- 【强制】不要用异常做流程控制、条件控制；`catch` 时区分稳定代码与非稳定代码。
- 【强制】禁止在 `finally` 块中使用 `return`。
- 【强制】应用中不可直接使用日志系统（Log4j、Logback）的 API，统一依赖 SLF4J。
- 【强制】异常信息应包含**案发现场信息**与**堆栈**，不要只输出 `e.getMessage()`。

**并发与集合**

- 【强制】线程资源必须通过线程池提供，禁止在应用中显式创建线程。
- 【强制】`SimpleDateFormat` 是线程不安全的类，禁止定义为 `static` 共享变量；日期处理优先使用 `java.time`。
- 【强制】使用 `Map` 时必须指定初始容量大小（`HashMap` 默认 16，扩容因子 0.75）。

**MySQL**

- 【强制】表名、字段名必须使用小写字母或数字，禁止数字开头，禁止两个下划线中间只出现数字。
- 【强制】表必备三字段：`id`、`gmt_create`、`gmt_modified`。本项目建表字段命名为 `create_time`、`update_time`，语义与手册一致，由 `MyMetaObjectHandler` 自动填充。
- 【强制】业务上具备唯一特性的字段，必须建成唯一索引，即便逻辑上已做控制。
- 【强制】数据库与表的字符集统一使用 `utf8mb4`。
- 【强制】唯一索引须覆盖真实业务约束：`im_user.account` 必须建唯一索引，否则并发注册时「先查后插」仍可能写入重复账号。
- 【强制】超过三个表禁止 `join`；`count(*)` 为统计行数首选。
- 逻辑删除字段统一为 `deleted`，`1` 为已删除、`0` 为未删除（由 MyBatis-Plus 全局配置生效）。

**单元测试**

- 【强制】单元测试必须遵守 **AIR 原则**：Automatic（全自动、非交互）、Independent（用例间不依赖）、Repeatable（可重复执行）。
- 【推荐】测试代码写在 `src/test/java` 下，与源代码目录结构保持一致。

---

## 8. 分支与提交规范

**分支模型**

| 分支 | 用途 | 规则 |
|---|---|---|
| `main` | 主干，始终可构建、可发布 | 只接受合并，不直接开发 |
| `feature/<功能名>` | 功能开发 | 从 `main` 切出，完成后合并回 `main` |
| `fix/<问题名>` | 缺陷修复 | 从 `main` 切出 |
| `hotfix/<问题名>` | 线上紧急修复 | 修复后同时合入 `main` |

**提交信息**采用 Conventional Commits 规范，格式 `type(scope): 描述`：

| type | 含义 |
|---|---|
| `feat` | 新功能 |
| `fix` | 缺陷修复 |
| `docs` | 文档变更 |
| `style` | 格式调整（不影响逻辑） |
| `refactor` | 重构（非新增功能、非修复缺陷） |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `build` | 构建配置、依赖变更 |
| `ci` | CI 配置变更 |
| `chore` | 杂项（不影响源码） |

示例：`feat(user): 新增手机号验证码登录接口`

---

## 9. 持续集成

`.github/workflows/ci.yml` 定义构建流水线，在以下时机自动触发：

- 推送 `main` 分支
- 推送 `v*.*.*` 版本标签
- 向 `main` 提交 Pull Request
- 手动 `workflow_dispatch` 触发

流水线行为：检出代码 → 安装 JDK 17（Maven 依赖缓存，缓存键依据 `server/pom.xml`）→ 在 `server/` 工作目录执行 `mvn -B -ntp verify`（编译、单元测试、打包，任一环节失败立即中断）→ 上传 `server/target/*.jar` 为构建产物（保留 7 天）。

> 前端 `web/` 暂未接入 CI；后续接入时可在同一 workflow 中增加独立 job，避免与后端构建互相阻塞。

安全约定：所有 action 以 **commit SHA 锁定**（版本见注释）；工作流权限收敛为 `contents: read`；同一分支连续推送时自动取消上一次未完成的运行，避免浪费额度。

构建产物下载路径：仓库 **Actions** 页 → 选择对应运行记录 → 底部 **Artifacts**。

---

## 10. 待办事项

- [x] 用户注册接口（BCrypt 加密、账号重复校验）
- [x] 用户登录 / JWT 鉴权 / 登出（Redis 会话白名单）
- [x] 开发与生产配置分离，生产凭据全部走环境变量
- [ ] `im_user.account` 建唯一索引，兜住并发注册
- [ ] 登录失败次数限制，防暴力破解
- [ ] 前端注册页与登录页接口联调
- [ ] WebSocket 长连接、心跳与消息收发
- [ ] 会话表、消息表设计与持久化
- [ ] 补充单元测试（AIR 原则，覆盖率不低于 70%）
- [ ] 数据库凭据改为环境变量注入，移除明文配置
- [ ] 上线前移除 `/test` 调试端点

---

## 11. 许可

本项目为个人学习与实践项目，暂未指定开源许可证。
