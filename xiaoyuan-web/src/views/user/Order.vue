<template>
  <div class="order-page">
    <div class="topbar"><el-button text @click="$router.back()"><el-icon><ArrowLeft /></el-icon></el-button><span>我的订单</span></div>

    <!-- 结算流程：先选地址，再确认支付 -->
    <div v-if="checkoutMode" class="checkout">
      <h3>确认订单</h3>
      <div class="section">
        <div class="section-title">收货地址</div>
        <div v-for="a in addresses" :key="a.id" class="addr-item" :class="{ active: selectedAddr === a.id }" @click="selectedAddr = a.id">
          <div><b>{{ a.consignee }}</b> {{ a.phone }}</div>
          <div class="addr-detail">{{ a.detail }} <el-tag v-if="a.isDefault === 1" size="small">默认</el-tag></div>
        </div>
        <el-button v-if="!addresses.length" text type="primary" @click="$router.push('/address')">+ 添加地址</el-button>
      </div>

      <div class="section">
        <div class="section-title">商品清单</div>
        <div v-for="item in checkoutItems" :key="item.id" class="co-item">
          <span>{{ item.productName }} × {{ item.quantity }}</span>
          <span>¥{{ (item.productPrice * item.quantity).toFixed(2) }}</span>
        </div>
        <div class="co-total">合计：<span class="price">¥{{ checkoutTotal }}</span></div>
      </div>

      <el-input v-model="remark" placeholder="备注（选填）" style="margin:8px 0" />
      <el-button type="danger" size="large" style="width:100%" :loading="submitting" @click="submitOrder">提交订单</el-button>
    </div>

    <!-- 订单列表 -->
    <template v-else>
      <div class="tabs">
        <span v-for="t in tabs" :key="t.value" :class="{ active: currentStatus === t.value }" @click="currentStatus = t.value; loadOrders(1)">{{ t.label }}</span>
      </div>
      <div v-if="!orders.length" class="empty"><el-empty description="暂无订单" /></div>
      <div v-for="o in orders" :key="o.id" class="order-card">
        <div class="order-head">
          <span class="order-no">订单号：{{ o.orderNo }}</span>
          <el-tag :type="statusMap[o.status]?.type">{{ statusMap[o.status]?.label }}</el-tag>
        </div>
        <div v-for="it in o.items" :key="it.id" class="order-item">
          <span>{{ it.productName }} × {{ it.quantity }}</span>
          <span>¥{{ it.subtotal }}</span>
        </div>
        <div class="order-bottom">
          <span class="amount">共{{ o.totalAmount }}件 合计：¥{{ o.totalAmount }}</span>
          <el-button v-if="o.status === 0" type="danger" size="small" @click="pay(o)">去支付</el-button>
          <el-button v-if="o.status === 0" size="small" @click="cancel(o)">取消</el-button>
          <el-button v-if="o.status === 4" size="small" type="primary" @click="toComment(o)">评价</el-button>
        </div>
      </div>
      <el-pagination v-if="total > pageSize" background layout="prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="loadOrders" style="justify-content:center; margin:12px 0" />
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getAddressList, createOrder, payOrder, cancelOrder, getOrderList, getCartList, createComment } from '../../api'

const route = useRoute()
const router = useRouter()

const checkoutMode = computed(() => !!route.query.cartIds)
const checkoutCartIds = computed(() => (route.query.cartIds || '').split(',').map(Number).filter(Boolean))

const tabs = [
  { label: '全部', value: null },
  { label: '待支付', value: 0 },
  { label: '待接单', value: 1 },
  { label: '配送中', value: 3 },
  { label: '已完成', value: 4 }
]
const statusMap = {
  0: { label: '待支付', type: 'warning' }, 1: { label: '已支付', type: 'primary' },
  2: { label: '已接单', type: 'primary' }, 3: { label: '配送中', type: 'info' },
  4: { label: '已完成', type: 'success' }, 5: { label: '已取消', type: 'info' }
}

const addresses = ref([])
const selectedAddr = ref(null)
const checkoutItems = ref([])
const checkoutTotal = ref('0.00')
const remark = ref('')
const submitting = ref(false)

const orders = ref([])
const currentStatus = ref(null)
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)

const loadCheckout = async () => {
  const [a, c] = await Promise.all([getAddressList(), getCartList()])
  addresses.value = a.data
  const def = a.data.find(x => x.isDefault === 1) || a.data[0]
  selectedAddr.value = def ? def.id : null
  checkoutItems.value = c.data.filter(i => checkoutCartIds.value.includes(i.id))
  checkoutTotal.value = checkoutItems.value.reduce((s, i) => s + i.productPrice * i.quantity, 0).toFixed(2)
}

const submitOrder = async () => {
  if (!selectedAddr.value) { ElMessage.warning('请选择收货地址'); return }
  if (!checkoutItems.value.length) { ElMessage.warning('没有可结算的商品'); return }
  submitting.value = true
  try {
    await createOrder({ addressId: selectedAddr.value, remark: remark.value, cartIds: checkoutCartIds.value })
    ElMessage.success('下单成功')
    router.replace('/order')
  } finally { submitting.value = false }
}

const loadOrders = async (n) => {
  pageNum.value = n || pageNum.value
  const res = await getOrderList({ pageNum: pageNum.value, pageSize: pageSize.value, status: currentStatus.value })
  orders.value = res.data.list
  total.value = res.data.total
}

const pay = async (o) => {
  await ElMessageBox.confirm('确认使用余额支付 ¥' + o.totalAmount + ' 吗？', '支付确认', { type: 'warning' })
  await payOrder(o.id)
  ElMessage.success('支付成功')
  loadOrders()
}

const cancel = async (o) => {
  await ElMessageBox.confirm('确定取消该订单吗？', '提示', { type: 'warning' })
  await cancelOrder(o.id)
  ElMessage.success('已取消')
  loadOrders()
}

const toComment = async (o) => {
  const { value } = await ElMessageBox.prompt('请输入评价内容', '评价订单', { inputType: 'textarea' })
  await createComment({ productId: o.items?.[0]?.productId, orderId: o.id, content: value, score: 5 })
  ElMessage.success('评价成功')
}

onMounted(() => {
  if (checkoutMode.value) loadCheckout()
  else loadOrders(1)
})
</script>

<style scoped>
.order-page { max-width: 520px; margin: 0 auto; background: #fff; min-height: 100vh; }
.topbar { display: flex; align-items: center; padding: 10px 8px; }
.topbar span { font-weight: 600; }
.checkout { padding: 8px 14px 20px; }
.section { margin-bottom: 12px; }
.section-title { font-weight: 600; margin-bottom: 8px; color: #333; }
.addr-item { border: 1px solid #eee; border-radius: 8px; padding: 10px; margin-bottom: 8px; cursor: pointer; }
.addr-item.active { border-color: #ff6b6b; background: #fff5f5; }
.addr-detail { color: #999; font-size: 13px; margin-top: 4px; }
.co-item { display: flex; justify-content: space-between; padding: 6px 0; font-size: 14px; }
.co-total { text-align: right; margin-top: 8px; font-weight: 600; }
.price { color: #ff6b6b; font-weight: 700; }
.tabs { display: flex; gap: 4px; padding: 8px 12px; overflow-x: auto; }
.tabs span { white-space: nowrap; padding: 5px 12px; border-radius: 14px; font-size: 13px; cursor: pointer; color: #666; }
.tabs span.active { background: #ff6b6b; color: #fff; }
.empty { padding: 30px 0; }
.order-card { margin: 8px 12px; border: 1px solid #eee; border-radius: 8px; padding: 12px; }
.order-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.order-no { font-size: 12px; color: #999; }
.order-item { display: flex; justify-content: space-between; padding: 4px 0; font-size: 14px; }
.order-bottom { display: flex; justify-content: space-between; align-items: center; margin-top: 8px; border-top: 1px dashed #eee; padding-top: 8px; }
.amount { color: #666; font-size: 13px; }
</style>
