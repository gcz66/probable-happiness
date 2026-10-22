<template>
  <div class="detail">
    <div class="topbar"><el-button text @click="$router.back()"><el-icon><ArrowLeft /></el-icon></el-button><span>商品详情</span></div>
    <el-image :src="product.image" class="detail-img" fit="cover">
      <template #error><div class="img-placeholder">暂无图片</div></template>
    </el-image>
    <div class="detail-info">
      <div class="name">{{ product.name }}</div>
      <div class="price-row"><span class="price">¥{{ product.price }}</span><span class="stock">库存 {{ product.stock }}</span></div>
      <div class="desc">{{ product.description }}</div>
      <div class="sales">已售 {{ product.sales }} | 分类：{{ product.categoryName }}</div>
    </div>

    <!-- 评论列表 -->
    <div class="comments">
      <h3>用户评价（{{ comments.length }}）</h3>
      <el-empty v-if="!comments.length" description="暂无评价" :image-size="60" />
      <div v-for="c in comments" :key="c.id" class="comment-item">
        <div class="comment-head">
          <el-avatar :size="28">{{ (c.nickname || '用')[0] }}</el-avatar>
          <span>{{ c.nickname }}</span>
          <el-rate :model-value="c.score" disabled size="small" />
        </div>
        <div class="comment-content">{{ c.content }}</div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="bottom-bar">
      <div class="action" @click="toggleFavorite">
        <el-icon :color="isFav ? '#ff6b6b' : '#999'"><StarFilled v-if="isFav" /><Star v-else /></el-icon>
        <span>{{ isFav ? '已收藏' : '收藏' }}</span>
      </div>
      <div class="action" @click="showComment = true"><el-icon><ChatDotRound /></el-icon><span>评价</span></div>
      <el-input-number v-model="quantity" :min="1" :max="99" size="small" class="qty" />
      <el-button type="primary" color="#ff6b6b" @click="addCart">加入购物车</el-button>
      <el-button type="danger" @click="buyNow">立即购买</el-button>
    </div>

    <!-- 评价弹窗 -->
    <el-dialog v-model="showComment" title="发表评价" width="90%" style="max-width:400px">
      <el-rate v-model="commentScore" :texts="['很差','较差','一般','满意','超赞']" show-text />
      <el-input v-model="commentContent" type="textarea" :rows="4" placeholder="说说你的用餐体验吧" style="margin-top:12px" />
      <template #footer><el-button type="primary" @click="submitComment">提交</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Star, StarFilled, ChatDotRound } from '@element-plus/icons-vue'
import { getProductDetail, getProductComments, createComment, addFavorite, cancelFavorite, getFavoriteStatus, addToCart } from '../../api'

const route = useRoute()
const router = useRouter()
const product = ref({})
const comments = ref([])
const isFav = ref(false)
const quantity = ref(1)
const showComment = ref(false)
const commentScore = ref(5)
const commentContent = ref('')

const isLogin = () => !!localStorage.getItem('token')

const load = async () => {
  const id = route.params.id
  const res = await getProductDetail(id)
  product.value = res.data
  const c = await getProductComments(id)
  comments.value = c.data
  if (isLogin()) {
    try { const f = await getFavoriteStatus(id); isFav.value = f.data } catch {}
  }
}

const addCart = async () => {
  if (!isLogin()) { ElMessage.warning('请先登录'); router.push('/login'); return }
  await addToCart({ productId: product.value.id, quantity: quantity.value })
  ElMessage.success('已加入购物车')
}

const buyNow = () => {
  if (!isLogin()) { ElMessage.warning('请先登录'); router.push('/login'); return }
  addToCart({ productId: product.value.id, quantity: quantity.value }).then(() => router.push('/cart'))
}

const toggleFavorite = async () => {
  if (!isLogin()) { ElMessage.warning('请先登录'); router.push('/login'); return }
  if (isFav.value) { await cancelFavorite(product.value.id); isFav.value = false; ElMessage.success('已取消收藏') }
  else { await addFavorite(product.value.id); isFav.value = true; ElMessage.success('收藏成功') }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) { ElMessage.warning('请输入评价内容'); return }
  await createComment({ productId: product.value.id, content: commentContent.value, score: commentScore.value })
  ElMessage.success('评价成功')
  showComment.value = false
  commentContent.value = ''
  load()
}

onMounted(load)
</script>

<style scoped>
.detail { max-width: 520px; margin: 0 auto; background: #fff; min-height: 100vh; padding-bottom: 70px; }
.topbar { display: flex; align-items: center; padding: 10px 8px; position: sticky; top: 0; background: #fff; z-index: 5; }
.topbar span { font-weight: 600; }
.detail-img, .img-placeholder { width: 100%; height: 280px; }
.img-placeholder { display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; }
.detail-info { padding: 14px 16px; }
.name { font-size: 18px; font-weight: 700; }
.price-row { display: flex; align-items: center; justify-content: space-between; margin: 8px 0; }
.price { color: #ff6b6b; font-size: 20px; font-weight: 700; }
.stock, .sales { color: #999; font-size: 13px; }
.desc { color: #666; font-size: 14px; line-height: 1.6; }
.comments { padding: 14px 16px; border-top: 8px solid #f5f6f8; }
.comments h3 { margin-bottom: 10px; }
.comment-item { border-bottom: 1px solid #f0f0f0; padding: 10px 0; }
.comment-head { display: flex; align-items: center; gap: 8px; }
.comment-content { margin-top: 6px; color: #444; font-size: 14px; }
.bottom-bar { position: fixed; bottom: 0; left: 50%; transform: translateX(-50%); width: 520px; max-width: 100%; background: #fff; border-top: 1px solid #eee; display: flex; align-items: center; gap: 10px; padding: 10px 12px; z-index: 10; }
.action { display: flex; flex-direction: column; align-items: center; font-size: 12px; color: #999; cursor: pointer; }
.qty { width: 90px; }
</style>
