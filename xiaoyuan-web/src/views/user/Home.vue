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
        <el-image :src="p.image" class="product-img" fit="cover">
          <template #error><div class="img-placeholder">暂无图片</div></template>
        </el-image>
        <div class="product-info">
          <div class="product-name">{{ p.name }}</div>
          <div class="product-desc">{{ p.description }}</div>
          <div class="product-bottom">
            <span class="price">¥{{ p.price }}</span>
            <span class="sales">已售{{ p.sales }}</span>
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
      <div class="nav-item" :class="{ active: true }"><el-icon><HomeFilled /></el-icon><span>首页</span></div>
      <div class="nav-item" @click="$router.push('/cart')"><el-icon><ShoppingCart /></el-icon><span>购物车</span></div>
      <div class="nav-item" @click="$router.push('/order')"><el-icon><List /></el-icon><span>订单</span></div>
      <div class="nav-item" @click="$router.push('/profile')"><el-icon><User /></el-icon><span>我的</span></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { HomeFilled, ShoppingCart, List, User } from '@element-plus/icons-vue'
import { getBannerList, getCategoryList, getProductList } from '../../api'

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

onMounted(loadData)
</script>

<style scoped>
.home { max-width: 520px; margin: 0 auto; background: #fff; min-height: 100vh; padding-bottom: 60px; }
.topbar { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; background: #ff6b6b; color: #fff; position: sticky; top: 0; z-index: 10; }
.logo { font-weight: 700; font-size: 18px; }
.user { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.banner { margin: 12px 0; }
.banner-img { width: 100%; height: 160px; object-fit: cover; }
.banner-title { position: absolute; bottom: 8px; left: 12px; color: #fff; font-size: 14px; text-shadow: 0 1px 3px rgba(0,0,0,.5); }
.category-nav { display: flex; gap: 8px; overflow-x: auto; padding: 8px 12px; }
.cat-item { white-space: nowrap; padding: 6px 14px; border-radius: 16px; background: #f5f6f8; cursor: pointer; font-size: 14px; }
.cat-item.active { background: #ff6b6b; color: #fff; }
.product-list { display: flex; flex-wrap: wrap; gap: 12px; padding: 12px; }
.product-card { width: calc(50% - 6px); border: 1px solid #eee; border-radius: 8px; overflow: hidden; cursor: pointer; }
.product-img, .img-placeholder { width: 100%; height: 130px; }
.img-placeholder { display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; font-size: 13px; }
.product-info { padding: 8px 10px; }
.product-name { font-size: 15px; font-weight: 600; }
.product-desc { font-size: 12px; color: #999; margin: 4px 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-bottom { display: flex; justify-content: space-between; align-items: center; }
.price { color: #ff6b6b; font-weight: 700; font-size: 16px; }
.sales { font-size: 12px; color: #aaa; }
.bottom-nav { position: fixed; bottom: 0; left: 50%; transform: translateX(-50%); width: 520px; max-width: 100%; display: flex; background: #fff; border-top: 1px solid #eee; z-index: 10; }
.nav-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 6px 0; font-size: 12px; color: #999; cursor: pointer; }
.nav-item.active { color: #ff6b6b; }
</style>
