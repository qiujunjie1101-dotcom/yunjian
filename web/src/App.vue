<script setup>
// 最外层壳：只负责「显示哪个页面」，不写任何业务代码
// 类比 YunJianApplication.main() 之后那个空的 ApplicationContext
// 注意：S1 之前还没有 @ 别名，所以这里先用相对路径
import RegisterView from './views/RegisterView.vue'
</script>

<template>
  <RegisterView />
</template>
