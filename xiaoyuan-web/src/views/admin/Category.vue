<template>
  <div>
    <div class="toolbar"><el-button type="success" @click="openDialog()">新增分类</el-button></div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="分类名称" min-width="140" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="状态" width="80">
        <template #default="{row}"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="form.id ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" label-width="70px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
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
import { getAdminCategoryList, createCategory, updateCategory, deleteCategory } from '../../api'

const list = ref([])
const dialog = ref(false)
const form = reactive({ id: null, name: '', sort: 0, status: 1 })

const load = async () => { const r = await getAdminCategoryList(); list.value = r.data }
const openDialog = (row) => {
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, name: '', sort: 0, status: 1 })
  dialog.value = true
}
const save = async () => {
  if (!form.name) { ElMessage.warning('请输入分类名称'); return }
  if (form.id) await updateCategory(form.id, form)
  else await createCategory(form)
  ElMessage.success('保存成功')
  dialog.value = false
  load()
}
const remove = async (row) => {
  await ElMessageBox.confirm(`确定删除分类「${row.name}」？分类下若有商品将无法删除`, '提示', { type: 'warning' })
  await deleteCategory(row.id)
  ElMessage.success('已删除')
  load()
}
onMounted(load)
</script>

<style scoped>
.toolbar { margin-bottom: 12px; }
</style>
