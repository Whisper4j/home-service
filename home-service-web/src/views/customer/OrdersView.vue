<script setup lang="ts">
import { ref, watch } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import { sessions } from '../../stores/session'
import QuoteDialog from '../../components/QuoteDialog.vue'
import Feedback from '../../components/CustomerFeedback.vue'
const groups: { name: string; statuses?: Schema['OrderStatus'][] }[] = [
  { name: '全部' },
  { name: '待支付', statuses: ['PENDING_PAYMENT'] },
  { name: '待安排', statuses: ['WAITING_DISPATCH', 'WAITING_ACCEPTANCE'] },
  { name: '进行中', statuses: ['PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE'] },
  { name: '待确认', statuses: ['PENDING_CONFIRMATION'] },
  { name: '已结束', statuses: ['COMPLETED', 'CANCELLED'] },
]
const storageKey = `customer.orders.${sessions.customer?.account.id}`
const saved = JSON.parse(sessionStorage.getItem(storageKey) || '{}')
const quoteOrder = ref<Schema['OrderVO']>()
const group = ref(Number(saved.group) || 0),
  pageNo = ref(Number(saved.pageNo) || 1),
  page = ref<Schema['OrderPageDTO']>({ list: [], total: 0, pages: 0 }),
  { busy, error, run } = useTask()
watch([group, pageNo], () =>
  sessionStorage.setItem(storageKey, JSON.stringify({ group: group.value, pageNo: pageNo.value })),
)
function load() {
  return run(async () => {
    page.value = await request('customerListOrders', {
      query: {
        pageNo: pageNo.value,
        pageSize: 6,
        ...(groups[group.value].statuses ? { statuses: groups[group.value].statuses } : {}),
      },
    })
  })
}
function select(index: number) {
  group.value = index
  pageNo.value = 1
  void load()
}
function turn(delta: number) {
  pageNo.value += delta
  void load()
  document.getElementById('customer-scroll')?.scrollTo({ top: 0 })
}
function action(order: Schema['OrderVO']) {
  return order.status === 'PENDING_PAYMENT'
    ? '去支付'
    : order.status === 'PENDING_CONFIRMATION'
      ? '确认完成'
      : order.status === 'COMPLETED' && !order.reviewed
        ? '去评价'
        : order.status === 'WAITING_ACCEPTANCE'
          ? '查看进度'
          : '查看进度'
}
useRefresh(load)
</script>
<template>
  <div class="order-groups" aria-label="订单分组">
    <button
      v-for="(item, index) in groups"
      :key="item.name"
      :aria-pressed="index === group"
      :disabled="busy"
      @click="select(index)"
    >
      {{ item.name }}
    </button>
  </div>
  <Feedback :busy="busy" :error="error" />
  <button v-if="error" @click="load">重新加载订单</button>
  <article v-for="order in page.list" :key="order.id" class="panel order-card">
    <div class="card-heading">
      <h2>{{ order.service.skuName }}</h2>
      <span class="badge">{{ label(order.status) }}</span>
    </div>
    <p>{{ displayTime(order.startTime) }}</p>
    <p>¥{{ order.dealPrice || order.currentPrice }} · {{ label(order.bookingType) }}</p>
    <p v-if="order.workerName">服务人员：{{ order.workerName }}</p>
    <RouterLink
      class="button wide-button"
      :to="
        order.status === 'PENDING_PAYMENT'
          ? `/customer/pay/${order.id}`
          : `/customer/orders/${order.id}`
      "
    >
      {{ action(order) }}
    </RouterLink>
    <button
      v-if="order.status === 'WAITING_ACCEPTANCE'"
      class="wide-button"
      @click="quoteOrder = order"
    >
      调整报价
    </button>
  </article>
  <QuoteDialog
    v-if="quoteOrder"
    :order="page.list.find((o) => o.id === quoteOrder?.id) || quoteOrder"
    @close="quoteOrder = undefined"
    @saved="load"
  />
  <p v-if="!busy && !error && !page.list.length" class="empty">
    这里还没有订单。
    <RouterLink to="/customer/home">去选择服务</RouterLink>
  </p>
  <div v-if="page.pages" class="pagination">
    <button :disabled="busy || pageNo <= 1" @click="turn(-1)">上一页</button>
    <span>{{ pageNo }} / {{ page.pages }} · {{ page.total }}单</span>
    <button :disabled="busy || pageNo >= page.pages" @click="turn(1)">下一页</button>
  </div>
</template>
