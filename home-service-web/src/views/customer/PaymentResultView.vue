<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { label } from '../../utils/labels'
import { displayTime } from '../../utils/format'
import Feedback from '../../components/Feedback.vue'
const id = String(useRoute().params.id),
  order = ref<Schema['OrderVO']>(),
  task = useTask()
function load() {
  return task.run(async () => {
    order.value = await request('customerGetOrder', { id })
  })
}
useRefresh(load)
function progress(event: Event) {
  if ((event as CustomEvent<Schema['WsEvent']>).detail.orderId === id) void load()
}
onMounted(() => window.addEventListener('business-notification', progress))
onUnmounted(() => window.removeEventListener('business-notification', progress))
</script>
<template>
  <Feedback :busy="task.busy.value" :error="task.error.value" />
  <button @click="load">查询支付与安排结果</button>
  <section v-if="order" class="booking-section">
    <h2>{{ label(order.paymentStatus) }}</h2>
    <p>{{ order.service.skuName }} · ¥{{ order.currentPrice }}</p>
    <p>{{ label(order.status) }}</p>
    <p v-if="order.workerName">已安排：{{ order.workerName }}</p>
    <p v-if="order.status === 'WAITING_DISPATCH'">
      正在安排人员，每30秒重试；最晚到
      {{ displayTime(order.dispatchDeadline) }}，5分钟失败将取消并全额模拟退款。
    </p>
    <p v-if="order.status === 'WAITING_ACCEPTANCE'">
      等待人员自主接单，截止
      {{ displayTime(order.offerDeadline) }}。不保证接单，可在订单进度中调价或取消。
    </p>
    <p v-if="order.cancellationReason">{{ order.cancellationReason }}</p>
    <p v-if="order.paymentStatus === 'REFUNDED'">已全额模拟退款。</p>
    <RouterLink class="button wide-button" replace :to="`/customer/orders/${id}`">
      查看订单进度
    </RouterLink>
  </section>
  <Teleport to="#customer-actions" defer>
    <RouterLink replace class="button wide-button" to="/customer/home">返回首页</RouterLink>
  </Teleport>
</template>
