<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2>校园外卖平台</h2>
      <p class="sub">用户登录</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" :prefix-icon="Lock" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button>
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
.auth-page { height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #ff9a56 0%, #ff6b6b 100%); }
.auth-card { width: 380px; background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 8px 30px rgba(0,0,0,.15); }
.auth-card h2 { text-align: center; color: #ff6b6b; }
.auth-card .sub { text-align: center; color: #999; margin: 8px 0 24px; }
.links { margin-top: 16px; display: flex; justify-content: space-between; font-size: 13px; }
.links a { color: #409eff; text-decoration: none; }
</style>
