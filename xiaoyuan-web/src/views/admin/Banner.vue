<template>
  <div>
    <div class="toolbar"><el-button type="success" @click="openDialog()">新增轮播图</el-button></div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="120" />
      <el-table-column label="图片" width="120">
        <template #default="{row}"><el-image :src="row.image" style="width:80px;height:40px" fit="cover" /></template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{row}"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '显示' : '隐藏' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="form.id ? '编辑轮播图' : '新增轮播图'" width="420px">
      <el-form :model="form" label-width="70px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图片URL"><el-input v-model="form.image" placeholder="https://..." /></el-form-item>
        <el-form-item label="跳转链接"><el-input v-model="form.linkUrl" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminBannerList, createBanner, updateBanner, deleteBanner } from '../../api'

const list = ref([])
const dialog = ref(false)
const form = reactive({ id: null, title: '', image: '', linkUrl: '', sort: 0, status: 1 })

const load = async () => { const r = await getAdminBannerList(); list.value = r.data }
const openDialog = (row) => {
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, title: '', image: '', linkUrl: '', sort: 0, status: 1 })
  dialog.value = true
}
const save = async () => {
  if (!form.image) { ElMessage.warning('请输入图片URL'); return }
  if (form.id) await updateBanner(form.id, form)
  else await createBanner(form)
  ElMessage.success('保存成功')
  dialog.value = false
  load()
}
const remove = async (row) => {
  await ElMessageBox.confirm('确定删除该轮播图？', '提示', { type: 'warning' })
  await deleteBanner(row.id)
  ElMessage.success('已删除')
  load()
}
onMounted(load)
</script>

<style scoped>
.toolbar { margin-bottom: 12px; }
</style>
