<template>
  <div>
    <div class="toolbar">
      <el-input v-model="keyword" placeholder="商品名称" style="width:180px" clearable @clear="load(1)" @keyup.enter="load(1)" />
      <el-select v-model="filterCat" placeholder="全部分类" clearable style="width:140px" @change="load(1)">
        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <el-button type="primary" @click="load(1)">查询</el-button>
      <el-button type="success" @click="openDialog()">新增商品</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="名称" min-width="120" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column label="价格" width="90"><template #default="{row}">¥{{ row.price }}</template></el-table-column>
      <el-table-column prop="stock" label="库存" width="70" />
      <el-table-column prop="sales" label="销量" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{row}"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '上架' : '下架' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="pager" background layout="total, prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="load" />

    <el-dialog v-model="dialog" :title="form.id ? '编辑商品' : '新增商品'" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称" required><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="分类" required>
          <el-select v-model="form.categoryId" style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="图片URL"><el-input v-model="form.image" placeholder="https://..." /></el-form-item>
        <el-form-item label="价格" required><el-input-number v-model="form.price" :min="0" :precision="2" :step="1" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="上架" inactive-text="下架" />
        </el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminProductList, createProduct, updateProduct, deleteProduct, getAdminCategoryList } from '../../api'

const list = ref([])
const categories = ref([])
const keyword = ref('')
const filterCat = ref(null)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialog = ref(false)
const form = reactive({ id: null, name: '', categoryId: null, image: '', price: 0, stock: 0, description: '', status: 1 })

const load = async (n) => {
  pageNum.value = n || pageNum.value
  const r = await getAdminProductList({ pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value, categoryId: filterCat.value })
  list.value = r.data.list
  total.value = r.data.total
}

const openDialog = (row) => {
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, name: '', categoryId: null, image: '', price: 0, stock: 0, description: '', status: 1 })
  dialog.value = true
}

const save = async () => {
  if (!form.name || !form.categoryId) { ElMessage.warning('请填写名称和分类'); return }
  if (form.id) await updateProduct(form.id, form)
  else await createProduct(form)
  ElMessage.success('保存成功')
  dialog.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确定删除商品「${row.name}」？相关收藏和评论将一并删除`, '提示', { type: 'warning' })
  await deleteProduct(row.id)
  ElMessage.success('已删除')
  load()
}

onMounted(async () => {
  const c = await getAdminCategoryList()
  categories.value = c.data
  load(1)
})
</script>

<style scoped>
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; align-items: center; }
.pager { margin-top: 12px; justify-content: flex-end; }
</style>
