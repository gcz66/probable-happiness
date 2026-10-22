<template>
  <div>
    <div class="toolbar">
      <el-input v-model="keyword" placeholder="评论内容/用户" style="width:200px" clearable @keyup.enter="load" />
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="nickname" label="用户" width="100" />
      <el-table-column prop="productName" label="商品" width="120" />
      <el-table-column label="评分" width="90"><template #default="{row}"><el-rate :model-value="row.score" disabled size="small" /></template></el-table-column>
      <el-table-column prop="content" label="内容" min-width="200" />
      <el-table-column prop="createTime" label="时间" width="160" />
      <el-table-column label="操作" width="90">
        <template #default="{row}"><el-button size="small" type="danger" @click="remove(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminCommentList, deleteComment } from '../../api'

const list = ref([])
const keyword = ref('')

const load = async () => {
  const r = await getAdminCommentList({ keyword: keyword.value })
  list.value = r.data
}
const remove = async (row) => {
  await ElMessageBox.confirm('确定删除该评论？', '提示', { type: 'warning' })
  await deleteComment(row.id)
  ElMessage.success('已删除')
  load()
}
onMounted(load)
</script>

<style scoped>
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; }
</style>
