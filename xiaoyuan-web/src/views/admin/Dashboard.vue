<template>
  <div class="dashboard">
    <!-- 指标卡片 -->
    <el-row :gutter="16">
      <el-col :span="6" v-for="card in cards" :key="card.label">
        <el-card shadow="hover">
          <div class="metric">
            <div class="metric-label">{{ card.label }}</div>
            <div class="metric-value" :style="{ color: card.color }">{{ card.value }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover"><template #header>近7日订单量与营业额</template><div ref="trendRef" class="chart" /></el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover"><template #header>订单状态分布</template><div ref="statusRef" class="chart" /></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover"><template #header>各分类商品数量</template><div ref="categoryRef" class="chart" /></el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover"><template #header>热门商品 Top5</template><div ref="hotRef" class="chart" /></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getOverview, getRecent7Days, getOrderStatus, getProductByCategory, getHotProducts } from '../../api'

const trendRef = ref()
const statusRef = ref()
const categoryRef = ref()
const hotRef = ref()

const cards = reactive([
  { label: '今日订单', value: 0, color: '#ff6b6b' },
  { label: '今日营业额(¥)', value: 0, color: '#ff9a56' },
  { label: '总订单数', value: 0, color: '#409eff' },
  { label: '总用户数', value: 0, color: '#67c23a' }
])

const charts = []

const renderTrend = (data) => {
  const c = echarts.init(trendRef.value)
  c.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['订单量', '营业额'] },
    grid: { left: 40, right: 20, top: 40, bottom: 30 },
    xAxis: { type: 'category', data: data.map(d => d.date) },
    yAxis: [{ type: 'value' }, { type: 'value' }],
    series: [
      { name: '订单量', type: 'bar', data: data.map(d => d.orders) },
      { name: '营业额', type: 'line', yAxisIndex: 1, data: data.map(d => Number(d.amount)) }
    ]
  })
  charts.push(c)
}

const renderStatus = (data) => {
  const names = ['待支付', '已支付', '已接单', '配送中', '已完成', '已取消']
  const c = echarts.init(statusRef.value)
  c.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie', radius: '60%',
      data: data.map(d => ({ name: names[d.status] || '未知', value: d.num })),
      label: { formatter: '{b}: {c}' }
    }]
  })
  charts.push(c)
}

const renderCategory = (data) => {
  const c = echarts.init(categoryRef.value)
  c.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.category_name) },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: data.map(d => d.num), itemStyle: { color: '#ff9a56' } }]
  })
  charts.push(c)
}

const renderHot = (data) => {
  const c = echarts.init(hotRef.value)
  c.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: data.map(d => d.name).reverse() },
    series: [{ type: 'bar', data: data.map(d => d.sales).reverse(), itemStyle: { color: '#ff6b6b' } }]
  })
  charts.push(c)
}

const resize = () => charts.forEach(c => c.resize())

onMounted(async () => {
  const [ov, trend, status, cat, hot] = await Promise.all([
    getOverview(), getRecent7Days(), getOrderStatus(), getProductByCategory(), getHotProducts({ topN: 5 })
  ])
  const o = ov.data
  cards[0].value = o.todayOrders
  cards[1].value = o.todayAmount
  cards[2].value = o.totalOrders
  cards[3].value = o.totalUsers
  renderTrend(trend.data)
  renderStatus(status.data)
  renderCategory(cat.data)
  renderHot(hot.data)
  window.addEventListener('resize', resize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
  charts.forEach(c => c.dispose())
})
</script>

<style scoped>
.metric { text-align: center; }
.metric-label { color: #999; font-size: 13px; margin-bottom: 8px; }
.metric-value { font-size: 24px; font-weight: 700; }
.chart-row { margin-top: 16px; }
.chart { height: 320px; }
</style>
