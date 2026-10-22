<template>
  <div class="profile">
    <div class="header">
      <div class="deco deco-1"></div>
      <div class="deco deco-2"></div>
      <el-avatar :size="64" :src="user?.avatar" class="avatar">{{ (user?.nickname || user?.username || '用')[0] }}</el-avatar>
      <div class="info">
        <div class="name">{{ user?.nickname || user?.username }}</div>
        <div class="balance"><span class="label">账户余额</span><b>¥{{ user?.balance }}</b></div>
      </div>
    </div>

    <div class="stat-card">
      <div class="stat-item" @click="go('/order')">
        <div class="stat-num">{{ orderCount || 0 }}</div><div class="stat-label">订单</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item" @click="go('/favorite')">
        <div class="stat-num">{{ favCount || 0 }}</div><div class="stat-label">收藏</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item" @click="go('/cart')">
        <div class="stat-num">{{ cartCount || 0 }}</div><div class="stat-label">购物车</div>
      </div>
    </div>

    <div class="menu">
      <div class="menu-item" @click="go('/order')"><span class="icon" style="background:#fff1f0;color:#ff6b6b"><el-icon><List /></el-icon></span><span>我的订单</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="go('/cart')"><span class="icon" style="background:#fff7e6;color:#fa8c16"><el-icon><ShoppingCart /></el-icon></span><span>购物车</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="go('/favorite')"><span class="icon" style="background:#fff0f6;color:#eb2f96"><el-icon><Star /></el-icon></span><span>我的收藏</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="go('/address')"><span class="icon" style="background:#e6f7ff;color:#1890ff"><el-icon><Location /></el-icon></span><span>收货地址</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="rechargeDialog = true"><span class="icon" style="background:#f6ffed;color:#52c41a"><el-icon><Wallet /></el-icon></span><span>余额充值</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
      <div class="menu-item" @click="pwdDialog = true"><span class="icon" style="background:#f0f0f0;color:#595959"><el-icon><Lock /></el-icon></span><span>修改密码</span><el-icon class="arrow"><ArrowRight /></el-icon></div>
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
import { getUserInfo, recharge, changePassword, getOrderList, getFavoriteList, getCartList } from '../../api'

const router = useRouter()
const user = ref({})
const rechargeDialog = ref(false)
const rechargeAmount = ref(10)
const recharging = ref(false)
const pwdDialog = ref(false)
const pwd = ref({ oldPassword: '', newPassword: '' })
const orderCount = ref(0)
const favCount = ref(0)
const cartCount = ref(0)

const load = async () => {
  const r = await getUserInfo()
  user.value = r.data
  localStorage.setItem('userInfo', JSON.stringify(r.data))
  try { const o = await getOrderList({ pageNum: 1, pageSize: 1 }); orderCount.value = o.data.total } catch {}
  try { const f = await getFavoriteList(); favCount.value = f.data.length } catch {}
  try { const c = await getCartList(); cartCount.value = c.data.length } catch {}
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
.profile { max-width: 520px; margin: 0 auto; background: var(--bg); min-height: 100vh; padding-bottom: 20px; }
.header { display: flex; align-items: center; gap: 16px; padding: 32px 20px 56px; background: var(--gradient); color: #fff; position: relative; overflow: hidden; }
.deco { position: absolute; border-radius: 50%; background: rgba(255,255,255,.1); }
.deco-1 { width: 180px; height: 180px; top: -60px; right: -40px; }
.deco-2 { width: 120px; height: 120px; bottom: -40px; left: -20px; }
.avatar { border: 3px solid rgba(255,255,255,.8); box-shadow: 0 4px 16px rgba(0,0,0,.2); position: relative; }
.info { position: relative; }
.name { font-size: 19px; font-weight: 700; }
.balance { margin-top: 6px; display: flex; align-items: baseline; gap: 8px; }
.balance .label { font-size: 12px; opacity: .85; }
.balance b { font-size: 22px; }
.stat-card { display: flex; align-items: center; justify-content: space-around; margin: -32px 16px 12px; background: #fff; border-radius: var(--card-radius); box-shadow: var(--shadow); padding: 16px 0; position: relative; z-index: 1; }
.stat-item { flex: 1; text-align: center; cursor: pointer; }
.stat-num { font-size: 20px; font-weight: 700; color: #ff6b6b; }
.stat-label { font-size: 12px; color: #999; margin-top: 2px; }
.stat-divider { width: 1px; height: 28px; background: #f0f0f0; }
.menu { margin: 8px 16px; background: #fff; border-radius: var(--card-radius); box-shadow: var(--shadow); overflow: hidden; }
.menu-item { display: flex; align-items: center; gap: 14px; padding: 15px 16px; border-bottom: 1px solid #f7f8fa; cursor: pointer; transition: background .2s; }
.menu-item:last-child { border-bottom: none; }
.menu-item:hover { background: #fafbfc; }
.icon { width: 34px; height: 34px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 17px; flex-shrink: 0; }
.menu-item span:nth-child(2) { font-size: 15px; }
.menu-item .arrow { margin-left: auto; color: #ccc; }
.amounts { margin-bottom: 8px; }
</style>
