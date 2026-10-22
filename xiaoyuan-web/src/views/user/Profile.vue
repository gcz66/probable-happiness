<template>
  <div class="profile">
    <div class="header">
      <el-avatar :size="60" :src="user?.avatar">{{ (user?.nickname || user?.username || '用')[0] }}</el-avatar>
      <div class="info">
        <div class="name">{{ user?.nickname || user?.username }}</div>
        <div class="balance">余额：<b>¥{{ user?.balance }}</b></div>
      </div>
    </div>

    <div class="menu">
      <div class="menu-item" @click="go('/order')"><el-icon><List /></el-icon><span>我的订单</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="go('/cart')"><el-icon><ShoppingCart /></el-icon><span>购物车</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="go('/favorite')"><el-icon><Star /></el-icon><span>我的收藏</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="go('/address')"><el-icon><Location /></el-icon><span>收货地址</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="rechargeDialog = true"><el-icon><Wallet /></el-icon><span>余额充值</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="pwdDialog = true"><el-icon><Lock /></el-icon><span>修改密码</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
    </div>

    <el-button style="margin: 16px; width: calc(100% - 32px)" @click="logout">退出登录</el-button>

    <!-- 充值弹窗 -->
    <el-dialog v-model="rechargeDialog" title="余额充值" width="90%" style="max-width:360px">
      <el-radio-group v-model="rechargeAmount" class="amounts">
        <el-radio-button v-for="n in [10, 20, 50, 100]" :key="n" :label="n">¥{{ n }}</el-radio-button>
      </el-radio-group>
      <template #footer>
        <el-button type="danger" :loading="recharging" @click="doRecharge">确认充值 ¥{{ rechargeAmount }}</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="pwdDialog" title="修改密码" width="90%" style="max-width:360px">
      <el-input v-model="pwd.oldPassword" type="password" show-password placeholder="原密码" style="margin-bottom:10px" />
      <el-input v-model="pwd.newPassword" type="password" show-password placeholder="新密码" />
      <template #footer><el-button type="primary" @click="changePwd">确认修改</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { List, ShoppingCart, Star, Location, Wallet, Lock, ArrowRight } from '@element-plus/icons-vue'
import { getUserInfo, recharge, changePassword } from '../../api'

const router = useRouter()
const user = ref({})
const rechargeDialog = ref(false)
const rechargeAmount = ref(10)
const recharging = ref(false)
const pwdDialog = ref(false)
const pwd = ref({ oldPassword: '', newPassword: '' })

const load = async () => {
  const r = await getUserInfo()
  user.value = r.data
  localStorage.setItem('userInfo', JSON.stringify(r.data))
}
const go = (p) => router.push(p)
const logout = () => {
  localStorage.removeItem('token'); localStorage.removeItem('role'); localStorage.removeItem('userInfo')
  ElMessage.success('已退出')
  router.push('/home')
}
const doRecharge = async () => {
  recharging.value = true
  try {
    await recharge(rechargeAmount.value)
    ElMessage.success('充值成功')
    rechargeDialog.value = false
    load()
  } finally { recharging.value = false }
}
const changePwd = async () => {
  if (!pwd.value.newPassword || pwd.value.newPassword.length < 6) { ElMessage.warning('新密码至少6位'); return }
  await changePassword(pwd.value)
  ElMessage.success('修改成功，请重新登录')
  logout()
}
onMounted(load)
</script>

<style scoped>
.profile { max-width: 520px; margin: 0 auto; background: #fff; min-height: 100vh; }
.header { display: flex; align-items: center; gap: 16px; padding: 24px 20px; background: linear-gradient(135deg, #ff9a56, #ff6b6b); color: #fff; }
.name { font-size: 18px; font-weight: 700; }
.balance { margin-top: 6px; }
.balance b { font-size: 20px; }
.menu { padding: 8px 0; }
.menu-item { display: flex; align-items: center; gap: 12px; padding: 14px 20px; border-bottom: 1px solid #f5f6f8; cursor: pointer; }
.menu-item .arrow { margin-left: auto; color: #ccc; }
.amounts { margin-bottom: 8px; }
</style>
