<template>
  <div class="auth-page">
    <div class="deco deco-1"></div>
    <div class="deco deco-2"></div>
    <div class="auth-card">
      <div class="logo-badge">🍔</div>
      <h2>校园外卖平台</h2>
      <p class="sub">注册账号，开启美食之旅</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" size="large" />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码（至少6位）" :prefix-icon="Lock" size="large" />
        </el-form-item>
        <el-button type="primary" style="width:100%" size="large" :loading="loading" @click="handleRegister">注 册</el-button>
      </el-form>
      <div class="links">
        <router-link to="/login">已有账号？去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { userRegister } from '../../api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', nickname: '', phone: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }]
}

const handleRegister = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await userRegister(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
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
.auth-card { width: 380px; background: #fff; border-radius: 20px; padding: 32px 36px 24px; box-shadow: 0 20px 60px rgba(0,0,0,.18); position: relative; z-index: 1; }
.logo-badge { width: 64px; height: 64px; margin: 0 auto 14px; border-radius: 50%; background: var(--gradient); display: flex; align-items: center; justify-content: center; font-size: 30px; box-shadow: 0 8px 20px rgba(255,107,107,.4); }
.auth-card h2 { text-align: center; color: #333; font-size: 20px; }
.auth-card .sub { text-align: center; color: #999; margin: 6px 0 20px; font-size: 13px; }
.auth-card :deep(.el-form-item) { margin-bottom: 16px; }
.links { margin-top: 16px; text-align: center; font-size: 13px; }
.links a { color: #409eff; text-decoration: none; }
</style>
