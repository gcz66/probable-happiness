<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2>校园外卖平台</h2>
      <p class="sub">管理端登录</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="管理员账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入管理员账号" :prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" :prefix-icon="Lock" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button>
      </el-form>
      <div class="links">
        <router-link to="/login">返回用户登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { adminLogin } from '../../api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await adminLogin(form)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('role', 'ADMIN')
    localStorage.setItem('adminInfo', JSON.stringify(res.data.admin))
    ElMessage.success('登录成功')
    router.push('/admin/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page { height: 100vh; display: flex; align-items: center; justify-content: center; background: #1f2d3d; }
.auth-card { width: 380px; background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 8px 30px rgba(0,0,0,.3); }
.auth-card h2 { text-align: center; color: #1f2d3d; }
.auth-card .sub { text-align: center; color: #999; margin: 8px 0 24px; }
.links { margin-top: 16px; text-align: center; font-size: 13px; }
.links a { color: #409eff; text-decoration: none; }
</style>
