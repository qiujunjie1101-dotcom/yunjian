import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Vite 默认只认识 .js / .html，vue 插件负责把 .vue 文件编译成浏览器能执行的代码。
// 第 03 步联调后端注册接口时，我们会在这里加 server.proxy 解决跨域。
export default defineConfig({
  plugins: [vue()],
})
