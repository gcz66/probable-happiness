<template>
  <router-view />
</template>

<script setup>
// 根组件
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
:root {
  --primary: #ff6b6b;
  --primary-deep: #ff4d4f;
  --gradient: linear-gradient(135deg, #ff9a56 0%, #ff6b6b 60%, #ff4d6d 100%);
  --bg: #f6f7fb;
  --card-radius: 16px;
  --shadow: 0 4px 16px rgba(31, 35, 65, .08);
  --shadow-hover: 0 8px 24px rgba(255, 107, 107, .18);
}
body {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
  background: var(--bg);
  color: #333;
  -webkit-font-smoothing: antialiased;
}
/* 全局按钮统一圆角与渐变 */
.el-button { border-radius: 10px; }
.el-button--primary { --el-button-bg-color: var(--primary); --el-button-border-color: var(--primary); --el-button-hover-bg-color: #ff8585; --el-button-hover-border-color: #ff8585; --el-button-active-bg-color: var(--primary-deep); --el-button-active-border-color: var(--primary-deep); }
.el-button--danger { --el-button-bg-color: var(--primary); --el-button-border-color: var(--primary); --el-button-hover-bg-color: #ff8585; --el-button-hover-border-color: #ff8585; --el-button-active-bg-color: var(--primary-deep); --el-button-active-border-color: var(--primary-deep); }
/* 输入框圆角 */
.el-input__wrapper, .el-textarea__inner { border-radius: 10px; }
/* 弹窗圆角 */
.el-dialog { border-radius: var(--card-radius); }
/* 滚动条美化 */
::-webkit-scrollbar { width: 6px; height: 6px; }
::-webkit-scrollbar-thumb { background: #ddd; border-radius: 3px; }
::-webkit-scrollbar-track { background: transparent; }
</style>
