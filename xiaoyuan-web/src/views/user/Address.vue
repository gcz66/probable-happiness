<template>
  <div class="addr">
    <div class="topbar"><el-button text @click="$router.back()"><el-icon><ArrowLeft /></el-icon></el-button><span>收货地址</span></div>
    <div v-for="a in list" :key="a.id" class="addr-item">
      <div class="addr-main" @click="edit(a)">
        <div><b>{{ a.consignee }}</b> {{ a.phone }} <el-tag v-if="a.isDefault === 1" size="small" type="danger">默认</el-tag></div>
        <div class="detail">{{ a.detail }}</div>
      </div>
      <el-button size="small" text type="danger" @click="remove(a)">删除</el-button>
    </div>
    <el-empty v-if="!list.length" description="暂无地址" />
    <div style="padding: 16px"><el-button type="danger" style="width:100%" @click="edit(null)">+ 新增地址</el-button></div>

    <el-dialog v-model="dialog" :title="form.id ? '编辑地址' : '新增地址'" width="90%" style="max-width:420px">
      <el-form :model="form" label-width="70px">
        <el-form-item label="收货人"><el-input v-model="form.consignee" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="form.detail" /></el-form-item>
        <el-form-item label="设为默认"><el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getAddressList, createAddress, updateAddress, deleteAddress } from '../../api'

const list = ref([])
const dialog = ref(false)
const form = reactive({ id: null, consignee: '', phone: '', detail: '', isDefault: 0 })

const load = async () => { const r = await getAddressList(); list.value = r.data }
const edit = (a) => {
  if (a) Object.assign(form, a)
  else Object.assign(form, { id: null, consignee: '', phone: '', detail: '', isDefault: 0 })
  dialog.value = true
}
const save = async () => {
  if (!form.consignee || !form.phone || !form.detail) { ElMessage.warning('请填写完整'); return }
  if (form.id) await updateAddress(form.id, form)
  else await createAddress(form)
  ElMessage.success('保存成功')
  dialog.value = false
  load()
}
const remove = async (a) => {
  await ElMessageBox.confirm('确定删除该地址？', '提示', { type: 'warning' })
  await deleteAddress(a.id)
  load()
}
onMounted(load)
</script>

<style scoped>
.addr { max-width: 520px; margin: 0 auto; background: #fff; min-height: 100vh; }
.topbar { display: flex; align-items: center; padding: 10px 8px; }
.topbar span { font-weight: 600; }
.addr-item { display: flex; align-items: center; padding: 12px 14px; border-bottom: 1px solid #f0f0f0; }
.addr-main { flex: 1; }
.detail { color: #999; font-size: 13px; margin-top: 4px; }
</style>
