<template>
  <div class="auth-page">
    <div class="deco deco-1"></div>
    <div class="deco deco-2"></div>
    <div class="auth-card">
      <div class="logo-badge">🍔</div>
      <h2>校园外卖平台</h2>
      <p class="sub">用户登录，美味即刻送达</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" :prefix-icon="Lock" size="large" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-button type="primary" style="width:100%" size="large" :loading="loading" @click="handleLogin">登 录</el-button>
      </el-form>
      <div class="links">
        <router-link to="/register">没有账号？立即注册</router-link>
        <router-link to="/admin/login">管理端入口</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { userLogin } from '../../api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await userLogin(form)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('role', 'USER')
    localStorage.setItem('userInfo', JSON.stringify(res.data.user))
    ElMessage.success('登录成功')
    router.push('/home')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page { height: 100vh; display: flex; align-items: center; justify-content: center; background: var(--gradient); position: relative; overflow: hidden; }
.deco { position: absolute; border-radius: 50%; background: rgba(255,255,255,.12); }
.deco-1 { width: 320px; height: 320px; top: -100px; right: -80px; }
.deco-2 { width: 220px; height: 220px; bottom: -60px; left: -40px; }
.auth-card { width: 380px; background: #fff; border-radius: 20px; padding: 36px 36px 28px; box-shadow: 0 20px 60px rgba(0,0,0,.18); position: relative; z-index: 1; }
.logo-badge { width: 64px; height: 64px; margin: 0 auto 14px; border-radius: 50%; background: var(--gradient); display: flex; align-items: center; justify-content: center; font-size: 30px; box-shadow: 0 8px 20px rgba(255,107,107,.4); }
.auth-card h2 { text-align: center; color: #333; font-size: 20px; }
.auth-card .sub { text-align: center; color: #999; margin: 6px 0 24px; font-size: 13px; }
.auth-card :deep(.el-form-item) { margin-bottom: 18px; }
.links { margin-top: 18px; display: flex; justify-content: space-between; font-size: 13px; }
.links a { color: #409eff; text-decoration: none; }
</style>
