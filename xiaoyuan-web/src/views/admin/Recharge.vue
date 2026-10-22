<template>
  <div>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="orderNo" label="流水号" min-width="180" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column label="金额" width="100">
        <template #default="{row}">
          <span :style="{ color: row.amount > 0 ? '#67c23a' : '#ff6b6b' }">
            {{ row.amount > 0 ? '+' : '' }}{{ row.amount }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="类型" width="90">
        <template #default="{row}">
          <el-tag :type="row.type === 1 ? 'success' : row.type === 2 ? 'danger' : 'warning'">
            {{ typeMap[row.type] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="balanceAfter" label="余额" width="90" />
      <el-table-column prop="remark" label="备注" min-width="140" />
      <el-table-column prop="createTime" label="时间" width="160" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminRechargeList } from '../../api'

const typeMap = { 1: '充值', 2: '消费', 3: '退款' }
const list = ref([])
onMounted(async () => { const r = await getAdminRechargeList(); list.value = r.data })
</script>
