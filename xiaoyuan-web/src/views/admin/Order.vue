<template>
  <div>
    <div class="toolbar">
      <el-select v-model="filterStatus" placeholder="全部状态" clearable style="width:130px" @change="load(1)">
        <el-option v-for="(v, k) in statusMap" :key="k" :label="v.label" :value="Number(k)" />
      </el-select>
      <el-input v-model="keyword" placeholder="订单号/用户名" style="width:200px" clearable @keyup.enter="load(1)" />
      <el-button type="primary" @click="load(1)">查询</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="orderNo" label="订单号" min-width="180" />
      <el-table-column prop="username" label="用户" width="100" />
      <el-table-column label="金额" width="100"><template #default="{row}">¥{{ row.totalAmount }}</template></el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{row}"><el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="160" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{row}">
          <el-button v-if="row.status === 1" size="small" @click="update(row, 2)">接单</el-button>
          <el-button v-if="row.status === 2" size="small" @click="update(row, 3)">配送</el-button>
          <el-button v-if="row.status === 3" size="small" type="success" @click="update(row, 4)">完成</el-button>
          <el-button v-if="row.status === 0" size="small" type="warning" @click="update(row, 5)">取消</el-button>
          <el-button size="small" @click="showDetail(row)">明细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="pager" background layout="total, prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNum" @current-change="load" />

    <el-dialog v-model="detailDialog" title="订单明细" width="480px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ detail.consignee }} {{ detail.phone }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail.addressDetail }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="detail.items" size="small" style="margin-top:10px">
        <el-table-column prop="productName" label="商品" min-width="120" />
        <el-table-column prop="quantity" label="数量" width="60" />
        <el-table-column prop="subtotal" label="小计" width="80" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminOrderList, updateOrderStatus, getOrderDetail } from '../../api'

const statusMap = {
  0: { label: '待支付', type: 'warning' }, 1: { label: '已支付', type: 'primary' },
  2: { label: '已接单', type: 'primary' }, 3: { label: '配送中', type: 'info' },
  4: { label: '已完成', type: 'success' }, 5: { label: '已取消', type: 'info' }
}
const list = ref([])
const filterStatus = ref(null)
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailDialog = ref(false)
const detail = ref({ items: [] })

const load = async (n) => {
  pageNum.value = n || pageNum.value
  const r = await getAdminOrderList({ pageNum: pageNum.value, pageSize: pageSize.value, status: filterStatus.value, keyword: keyword.value })
  list.value = r.data.list
  total.value = r.data.total
}
const update = async (row, status) => {
  await updateOrderStatus(row.id, { status })
  ElMessage.success('操作成功')
  load()
}
const showDetail = async (row) => {
  const r = await getOrderDetail(row.id)
  detail.value = r.data
  detailDialog.value = true
}
onMounted(() => load(1))
</script>

<style scoped>
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; }
.pager { margin-top: 12px; justify-content: flex-end; }
</style>
