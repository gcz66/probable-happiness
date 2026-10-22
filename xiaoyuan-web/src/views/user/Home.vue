<template>
  <div class="home">
    <!-- 顶部栏 -->
    <div class="topbar">
      <div class="logo">🍔 校园外卖</div>
      <div class="user" @click="goProfile">
        <el-avatar :size="32" :src="userInfo?.avatar">{{ (userInfo?.nickname || userInfo?.username || '客')[0] }}</el-avatar>
        <span v-if="isLogin">{{ userInfo?.nickname || userInfo?.username }}</span>
        <span v-else @click.stop="goLogin">登录</span>
      </div>
    </div>

    <!-- 轮播图 -->
    <el-carousel v-if="banners.length" height="160px" class="banner">
      <el-carousel-item v-for="b in banners" :key="b.id">
        <img :src="b.image" class="banner-img" />
        <div class="banner-title">{{ b.title }}</div>
      </el-carousel-item>
    </el-carousel>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="cat-item" :class="{ active: currentCat === null }" @click="selectCat(null)">全部</div>
      <div class="cat-item" v-for="c in categories" :key="c.id" :class="{ active: currentCat === c.id }" @click="selectCat(c.id)">
        {{ c.name }}
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="product-list">
      <div class="product-card" v-for="p in products" :key="p.id" @click="goDetail(p.id)">
        <div class="img-wrap">
          <el-image :src="p.image" class="product-img" fit="cover">
            <template #error><div class="img-placeholder">暂无图片</div></template>
          </el-image>
          <span class="sales-badge">已售{{ p.sales }}</span>
          <span v-if="p.price >= 20" class="hot-badge">热销</span>
        </div>
        <div class="product-info">
          <div class="product-name">{{ p.name }}</div>
          <div class="product-desc">{{ p.description }}</div>
          <div class="product-bottom">
            <span class="price"><i>¥</i>{{ p.price }}</span>
            <span class="add-btn" @click.stop="quickAdd(p)"><el-icon><Plus /></el-icon></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <el-pagination
      v-if="total > pageSize"
      background layout="prev, pager, next"
      :total="total" :page-size="pageSize" :current-page="pageNum"
      @current-change="handlePage"
    />

    <!-- 底部导航 -->
    <div class="bottom-nav">
      <div class="nav-item active"><el-icon><HomeFilled /></el-icon><span>首页</span></div>
      <div class="nav-item" @click="$router.push('/cart')"><el-icon><ShoppingCart /></el-icon><span>购物车</span></div>
      <div class="nav-item" @click="$router.push('/order')"><el-icon><List /></el-icon><span>订单</span></div>
      <div class="nav-item" @click="$router.push('/profile')"><el-icon><User /></el-icon><span>我的</span></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { HomeFilled, ShoppingCart, List, User, Plus } from '@element-plus/icons-vue'
import { getBannerList, getCategoryList, getProductList, addToCart } from '../../api'

const router = useRouter()
const banners = ref([])
const categories = ref([])
const products = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const currentCat = ref(null)
const keyword = ref('')

const userInfo = computed(() => {
  try { return JSON.parse(localStorage.getItem('userInfo')) } catch { return null }
})
const isLogin = computed(() => !!localStorage.getItem('token'))

const loadData = async () => {
  const [b, c, p] = await Promise.all([
    getBannerList(), getCategoryList(),
    getProductList({ pageNum: pageNum.value, pageSize: pageSize.value, categoryId: currentCat.value, keyword: keyword.value })
  ])
  banners.value = b.data
  categories.value = c.data
  products.value = p.data.list
  total.value = p.data.total
}

const selectCat = (id) => { currentCat.value = id; pageNum.value = 1; loadData() }
const handlePage = (n) => { pageNum.value = n; loadData() }
const goDetail = (id) => router.push(`/product/${id}`)
const goProfile = () => isLogin.value ? router.push('/profile') : goLogin()
const goLogin = () => router.push('/login')
const quickAdd = async (p) => {
  if (!isLogin.value) { ElMessage.warning('请先登录'); goLogin(); return }
  await addToCart({ productId: p.id, quantity: 1 })
  ElMessage.success('已加入购物车')
}

onMounted(loadData)
</script>

<style scoped>
.home { max-width: 520px; margin: 0 auto; background: #f6f7fb; min-height: 100vh; padding-bottom: 78px; }
.topbar { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; background: linear-gradient(135deg, #ff6b6b, #ff8e53); color: #fff; position: sticky; top: 0; z-index: 10; box-shadow: 0 2px 12px rgba(255,107,107,.35); }
.logo { font-weight: 700; font-size: 18px; letter-spacing: 1px; }
.user { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.banner { margin: 12px; }
.banner :deep(.el-carousel__item) { border-radius: 16px; overflow: hidden; box-shadow: 0 4px 16px rgba(0,0,0,.12); }
.banner-img { width: 100%; height: 160px; object-fit: cover; }
.banner-title { position: absolute; bottom: 10px; left: 12px; color: #fff; font-size: 15px; font-weight: 600; text-shadow: 0 1px 3px rgba(0,0,0,.5); }
.category-nav { display: flex; gap: 8px; overflow-x: auto; padding: 6px 12px 2px; }
.cat-item { white-space: nowrap; padding: 6px 14px; border-radius: 16px; background: #fff; border: 1px solid #eee; cursor: pointer; font-size: 14px; color: #555; transition: all .2s; }
.cat-item.active { background: linear-gradient(135deg, #ff6b6b, #ff8e53); color: #fff; border-color: transparent; font-weight: 600; box-shadow: 0 2px 8px rgba(255,107,107,.3); }
.product-list { display: flex; flex-wrap: wrap; gap: 12px; padding: 12px; }
.product-card { width: calc(50% - 6px); background: #fff; border-radius: 16px; overflow: hidden; cursor: pointer; box-shadow: 0 2px 10px rgba(0,0,0,.06); transition: transform .2s, box-shadow .2s; }
.product-card:active { transform: scale(.97); }
.img-wrap { position: relative; }
.product-img, .img-placeholder { width: 100%; height: 150px; display: block; }
.img-placeholder { display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; font-size: 13px; }
.sales-badge { position: absolute; left: 8px; bottom: 8px; background: rgba(0,0,0,.55); color: #fff; font-size: 11px; padding: 2px 8px; border-radius: 10px; backdrop-filter: blur(2px); }
.hot-badge { position: absolute; top: 8px; right: 8px; background: linear-gradient(135deg, #ff4d4f, #ff8e53); color: #fff; font-size: 11px; padding: 2px 8px; border-radius: 10px; box-shadow: 0 2px 6px rgba(255,77,79,.4); }
.product-info { padding: 10px 12px; }
.product-name { font-size: 15px; font-weight: 600; }
.product-desc { font-size: 12px; color: #999; margin: 4px 0 8px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-bottom { display: flex; justify-content: space-between; align-items: center; }
.price { color: #ff6b6b; font-weight: 700; font-size: 17px; }
.price i { font-style: normal; font-size: 12px; margin-right: 1px; }
.add-btn { width: 26px; height: 26px; border-radius: 50%; background: linear-gradient(135deg, #ff6b6b, #ff8e53); color: #fff; display: flex; align-items: center; justify-content: center; box-shadow: 0 2px 8px rgba(255,107,107,.4); transition: transform .15s; }
.add-btn:active { transform: scale(.85); }
.bottom-nav { position: fixed; bottom: 0; left: 50%; transform: translateX(-50%); width: calc(520px - 24px); max-width: calc(100% - 24px); margin: 0 12px 10px; display: flex; background: #fff; border-radius: 20px; box-shadow: 0 4px 20px rgba(0,0,0,.12); z-index: 10; padding: 6px 4px; }
.nav-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 6px 0; font-size: 12px; color: #999; cursor: pointer; border-radius: 16px; transition: all .2s; }
.nav-item .el-icon { font-size: 20px; transition: transform .2s; }
.nav-item.active { color: #ff6b6b; background: linear-gradient(135deg, rgba(255,107,107,.1), rgba(255,142,83,.1)); font-weight: 600; }
.nav-item.active .el-icon { transform: translateY(-1px); }
</style>
