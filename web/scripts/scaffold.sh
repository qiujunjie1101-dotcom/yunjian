#!/usr/bin/env bash
# 云笺 IM 前端 · 目录骨架脚本
#
# 用法：
#   bash scripts/scaffold.sh 1      创建第 1 阶段需要的目录
#   bash scripts/scaffold.sh all    创建全部目录（仅用于查看结构，不推荐实际使用）
#
# 只做两件事：建目录、放一个空的 .gitkeep。
# 不安装任何依赖，不写任何业务代码，不覆盖已有文件。
# 目录职责和阶段定义见 docs/ARCHITECTURE.md 第 2、4 节。

set -e

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
STAGE="${1:-}"

S1=( "public" "src/lib" )
S2=( "src/router" "src/views" "src/components/chat" )
S3=( "src/api" )
S4=( "src/stores" "src/composables" )
S5=( "src/components/ui" )

make_dirs() {
  for d in "$@"; do
    if [ -d "$ROOT/$d" ]; then
      echo "  跳过（已存在）  $d/"
    else
      mkdir -p "$ROOT/$d"
      printf '' > "$ROOT/$d/.gitkeep"
      echo "  新建            $d/"
    fi
  done
}

hint() {
  echo
  echo "本阶段接下来要做："
  for line in "$@"; do
    echo "  - $line"
  done
  echo "  详细步骤见 docs/ARCHITECTURE.md 第 4、5 节"
  echo
}

case "$STAGE" in
  1)
    echo "S1 样式与路径别名"
    make_dirs "${S1[@]}"
    hint \
      "npm install tailwindcss @tailwindcss/vite" \
      "按模板 5.1 / 5.2 / 5.3 修改 vite.config.js、新建 jsconfig.json 与 src/style.css" \
      "在 src/main.js 顶部加一行 import './style.css'" \
      "不要建 tailwind.config.js 和 postcss.config.js"
    ;;
  2)
    echo "S2 页面与路由"
    make_dirs "${S2[@]}"
    hint \
      "npm install vue-router@4" \
      "按模板 5.5 新建 src/router/index.js，在 main.js 里 use(router)" \
      "按模板 5.6 / 5.7 建立 views/ 与 components/chat/ 下的组件"
    ;;
  3)
    echo "S3 后端联调（本阶段不装任何依赖）"
    make_dirs "${S3[@]}"
    hint \
      "按模板 5.1 给 vite.config.js 加 server.proxy" \
      "按模板 5.8 / 5.9 新建 src/api/http.js 与 src/api/auth.js" \
      "新建 .env.example，并复制一份为 .env.development（后者不进 Git）"
    ;;
  4)
    echo "S4 共享状态"
    make_dirs "${S4[@]}"
    hint \
      "npm install pinia" \
      "在 main.js 里加 app.use(createPinia())" \
      "按模板 5.10 新建 stores/ 下的模块"
    ;;
  5)
    echo "S5 组件库"
    make_dirs "${S5[@]}"
    hint \
      "确认 S1 已完成（jsconfig.json、@ 别名、style.css 只有一行 import）" \
      "npx shadcn-vue@latest init，选项按 ARCHITECTURE.md 1.2 填" \
      "之后每次只加一个：npx shadcn-vue@latest add button" \
      "不要执行 add --all，不要手改 ui/ 下的文件名"
    ;;
  all)
    echo "注意：all 会一次性建出全部空目录，与「用到才引入」相悖，请确认你只是想看结构。"
    make_dirs "${S1[@]}" "${S2[@]}" "${S3[@]}" "${S4[@]}" "${S5[@]}"
    ;;
  *)
    echo "用法: bash scripts/scaffold.sh <1|2|3|4|5|all>"
    echo
    echo "  1  样式与路径别名     public/, src/lib/"
    echo "  2  页面与路由         src/router/, src/views/, src/components/chat/"
    echo "  3  后端联调           src/api/"
    echo "  4  共享状态           src/stores/, src/composables/"
    echo "  5  组件库             src/components/ui/"
    echo
    echo "当前只建议跑你正卡住的那一个阶段。"
    exit 1
    ;;
esac
