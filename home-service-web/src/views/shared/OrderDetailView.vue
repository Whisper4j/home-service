<script setup lang="ts">
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
import SceneImages from '../../components/SceneImages.vue'
import OrderStatusHistory from '../../components/OrderStatusHistory.vue'
const id = String(useRoute().params.id),
  order = ref<Schema['OrderVO']>(),
  history = ref<Schema['OrderHistoryVO']>(),
  reason = ref('')
const loader = useTask(),
  action = useTask()
async function refreshData() {
  const [detail, records] = await Promise.all([
    request('adminGetOrder', { id }),
    request('adminGetOrderHistory', { id }),
  ])
  order.value = detail
  history.value = records
}
function load() {
  return loader.run(refreshData)
}
function cancel() {
  return action.run(
    async (key) => {
      await request('cancelAdminOrder', { id, body: { reason: reason.value }, idempotencyKey: key })
      await refreshData()
    },
    '异常取消结果已确认',
    JSON.stringify({ id, reason: reason.value }),
  )
}
useRefresh(load)
</script>
<template>
  <div class="actions">
    <RouterLink to="/admin/orders">返回订单列表</RouterLink>
    <button :disabled="loader.busy.value" @click="load">重新查询</button>
  </div>
  <h1>订单详情 {{ id }}</h1>
  <Feedback :busy="loader.busy.value" :error="loader.error.value" />
  <template v-if="order">
    <section class="panel">
      <h2>{{ order.service.skuName }}</h2>
      <p>
        {{ label(order.status) }} · {{ label(order.bookingType) }} ·
        {{ label(order.paymentStatus) }} · {{ label(order.dispatchStatus) }}
      </p>
      <p>
        {{ displayTime(order.startTime) }} 至 {{ displayTime(order.endTime) }}；缓冲至
        {{ displayTime(order.bufferEndTime) }}
      </p>
      <p>
        当前价 ¥{{ order.currentPrice }}；成交价
        {{ order.dealPrice ? '¥' + order.dealPrice : '尚未锁定' }}
      </p>
      <p>人员：{{ order.workerName || '尚未安排' }} {{ order.workerId || '' }}</p>
      <p>本次联系人 {{ order.contactName }} · {{ order.contactPhone }}</p>
      <p>
        {{ order.address.provinceName }}{{ order.address.cityName }}{{ order.address.districtName
        }}{{ order.address.detail }}
      </p>
      <p>备注：{{ order.remark || '无' }}</p>
      <p>包含：{{ order.service.included }}；排除：{{ order.service.excluded }}</p>
      <p>客户自备配件：{{ order.service.customerSuppliesParts ? '需要' : '不需要' }}</p>
      <p v-if="order.cancellationReason">取消原因：{{ order.cancellationReason }}</p>
      <SceneImages :images="order.sceneImages" role="admin" :order-id="id" />
      <p>管理员仅处理异常取消，不提供手工派单或改派。</p>
      <form v-if="order.allowedActions.includes('CANCEL')" @submit.prevent="cancel">
        <label>
          异常取消原因
          <input v-model.trim="reason" required maxlength="300" />
        </label>
        <button :disabled="action.busy.value">确认异常取消</button>
      </form>
      <Feedback :error="action.error.value" :success="action.success.value" />
    </section>
    <template v-if="history">
      <OrderStatusHistory :entries="history.statusHistory" />
      <section class="panel">
        <h2>支付与退款记录</h2>
        <ul class="record-list">
          <li v-for="item in history.payments" :key="item.id">
            {{ displayTime(item.createdAt) }} · {{ label(item.type) }} ¥{{ item.amount }} · 业务号
            {{ item.businessNo }}
          </li>
        </ul>
        <p v-if="!history.payments.length">暂无支付记录</p>
        <h2>报价历史</h2>
        <ul class="record-list">
          <li v-for="item in history.priceHistory" :key="item.id">
            {{ displayTime(item.createdAt) }} · ¥{{ item.previousPrice }} → ¥{{ item.newPrice }}
          </li>
        </ul>
        <h2>分配历史</h2>
        <ul class="record-list">
          <li v-for="item in history.assignments" :key="item.id">
            {{ displayTime(item.assignedAt) }} · {{ item.workerName }} · {{ label(item.status) }}
            <p v-if="item.releasedAt">
              释放：{{ displayTime(item.releasedAt) }} · {{ item.releaseReason }}
            </p>
            <p v-if="item.finishedAt">完成：{{ displayTime(item.finishedAt) }}</p>
          </li>
        </ul>
        <p v-if="history.review">
          评价 {{ history.review.score }} 分 · {{ history.review.content }}
        </p>
      </section>
    </template>
  </template>
</template>
