<template>
  <el-container class="admin-layout">
    <el-aside width="200px" class="aside">
      <div class="logo">校园外卖 · 管理后台</div>
      <el-menu :default-active="$route.path" router background-color="#1f2d3d" text-color="#b0b8c4" active-text-color="#ff6b6b">
        <el-menu-item index="/admin/dashboard"><el-icon><DataAnalysis /></el-icon><span>数据看板</span></el-menu-item>
        <el-menu-item index="/admin/product"><el-icon><Goods /></el-icon><span>商品管理</span></el-menu-item>
        <el-menu-item index="/admin/category"><el-icon><Menu /></el-icon><span>分类管理</span></el-menu-item>
        <el-menu-item index="/admin/banner"><el-icon><Picture /></el-icon><span>轮播图管理</span></el-menu-item>
        <el-menu-item index="/admin/order"><el-icon><List /></el-icon><span>订单管理</span></el-menu-item>
        <el-menu-item index="/admin/comment"><el-icon><ChatDotRound /></el-icon><span>评论管理</span></el-menu-item>
        <el-menu-item index="/admin/recharge"><el-icon><Wallet /></el-icon><span>充值记录</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>{{ $route.meta.title }}</span>
        <div class="right">
          <el-button text @click="$router.push('/home')">前台首页</el-button>
          <el-button text @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { DataAnalysis, Goods, Menu, Picture, List, ChatDotRound, Wallet } from '@element-plus/icons-vue'

const router = useRouter()
const logout = () => {
  localStorage.removeItem('token'); localStorage.removeItem('role'); localStorage.removeItem('adminInfo')
  ElMessage.success('已退出')
  router.push('/admin/login')
}
</script>

<style scoped>
.admin-layout { height: 100vh; }
.aside { background: #1f2d3d; }
.aside .logo { color: #fff; font-weight: 700; text-align: center; padding: 20px 0; }
.header { background: #fff; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #eee; font-weight: 600; }
.main { background: #f5f6f8; }
</style>
