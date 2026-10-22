<template>
  <div class="fav">
    <div class="topbar"><el-button text @click="$router.back()"><el-icon><ArrowLeft /></el-icon></el-button><span>我的收藏</span></div>
    <el-empty v-if="!items.length" description="暂无收藏" />
    <div v-for="f in items" :key="f.id" class="fav-item" @click="$router.push(`/product/${f.productId}`)">
      <el-image :src="f.productImage" class="fav-img" fit="cover">
        <template #error><div class="img-placeholder">无图</div></template>
      </el-image>
      <div class="fav-info">
        <div class="name">{{ f.productName }}</div>
        <div class="price">¥{{ f.productPrice }}</div>
      </div>
      <el-button size="small" @click.stop="remove(f)">取消收藏</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getFavoriteList, cancelFavorite } from '../../api'

const items = ref([])
const load = async () => { const r = await getFavoriteList(); items.value = r.data }
const remove = async (f) => {
  await cancelFavorite(f.productId)
  ElMessage.success('已取消收藏')
  load()
}
onMounted(load)
</script>

<style scoped>
.fav { max-width: 520px; margin: 0 auto; background: #fff; min-height: 100vh; }
.topbar { display: flex; align-items: center; padding: 10px 8px; }
.topbar span { font-weight: 600; }
.fav-item { display: flex; align-items: center; gap: 10px; padding: 10px 14px; border-bottom: 1px solid #f0f0f0; cursor: pointer; }
.fav-img, .img-placeholder { width: 60px; height: 60px; border-radius: 6px; flex-shrink: 0; }
.img-placeholder { display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; font-size: 12px; }
.fav-info { flex: 1; }
.name { font-size: 15px; }
.price { color: #ff6b6b; font-weight: 600; margin-top: 4px; }
</style>
