<template>
  <div class="cart">
    <div class="topbar"><el-button text @click="$router.push('/home')"><el-icon><ArrowLeft /></el-icon></el-button><span>购物车</span></div>

    <div v-if="!items.length" class="empty"><el-empty description="购物车还是空的，去逛逛吧"><el-button type="primary" color="#ff6b6b" @click="$router.push('/home')">去逛逛</el-button></el-empty></div>

    <template v-else>
      <div class="cart-list">
        <div v-for="item in items" :key="item.id" class="cart-item">
          <el-checkbox :model-value="selected.has(item.id)" @change="v => toggle(item.id, v)" />
          <el-image :src="item.productImage" class="cart-img" fit="cover">
            <template #error><div class="img-placeholder">无图</div></template>
          </el-image>
          <div class="cart-info">
            <div class="name">{{ item.productName }}</div>
            <div class="price">¥{{ item.productPrice }}</div>
            <el-input-number :model-value="item.quantity" :min="1" :max="99" size="small" @change="v => changeQty(item, v)" />
          </div>
        </div>
      </div>

      <div class="checkout-bar">
        <el-checkbox :model-value="selected.size === items.length" @change="toggleAll">全选</el-checkbox>
        <div class="total">合计：<span class="price">¥{{ totalAmount }}</span></div>
        <el-button type="danger" round :disabled="!selected.size" @click="goCheckout">结算({{ selected.size }})</el-button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getCartList, updateCart } from '../../api'

const router = useRouter()
const items = ref([])
const selected = ref(new Set())

const load = async () => {
  const res = await getCartList()
  items.value = res.data
}

const toggle = (id, v) => {
  if (v) selected.value.add(id); else selected.value.delete(id)
  selected.value = new Set(selected.value)
}
const toggleAll = (v) => {
  if (v) selected.value = new Set(items.value.map(i => i.id))
  else selected.value = new Set()
}
const changeQty = async (item, v) => {
  item.quantity = v
  await updateCart(item.id, { quantity: v })
}
const totalAmount = computed(() => {
  let sum = 0
  items.value.filter(i => selected.value.has(i.id)).forEach(i => { sum += i.productPrice * i.quantity })
  return sum.toFixed(2)
})

const goCheckout = () => {
  if (!selected.value.size) return
  const cartIds = [...selected.value].join(',')
  router.push({ path: '/order', query: { cartIds } })
}

onMounted(load)
</script>

<style scoped>
.cart { max-width: 520px; margin: 0 auto; background: var(--bg); min-height: 100vh; padding-bottom: 80px; }
.topbar { display: flex; align-items: center; padding: 10px 8px; background: #fff; position: sticky; top: 0; z-index: 5; }
.topbar span { font-weight: 600; }
.empty { padding-top: 40px; }
.cart-list { padding: 4px 12px; }
.cart-item { display: flex; align-items: center; gap: 10px; padding: 12px; background: #fff; border-radius: var(--card-radius); box-shadow: var(--shadow); margin-bottom: 10px; }
.cart-img, .img-placeholder { width: 72px; height: 72px; border-radius: 12px; flex-shrink: 0; }
.img-placeholder { display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; font-size: 12px; }
.cart-info { flex: 1; }
.name { font-size: 15px; font-weight: 600; }
.price { color: #ff6b6b; font-weight: 700; margin: 4px 0; }
.checkout-bar { position: fixed; bottom: 0; left: 50%; transform: translateX(-50%); width: 520px; max-width: 100%; background: #fff; border-radius: 18px 18px 0 0; box-shadow: 0 -4px 20px rgba(0,0,0,.08); display: flex; align-items: center; gap: 12px; padding: 12px 16px; z-index: 10; }
.total { flex: 1; text-align: right; }
.total .price { font-size: 18px; }
</style>
