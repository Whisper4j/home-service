<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createIdempotencyKey, request, useMock } from '../../api/client'
import type { Schema } from '../../api/types'
import { sessions } from '../../stores/session'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import Feedback from '../../components/CustomerFeedback.vue'
const route = useRoute(),
  router = useRouter(),
  id = String(route.params.id),
  order = ref<Schema['OrderVO']>(),
  loader = useTask(),
  payment = useTask()
const storageKey = `home-service.payment.${sessions.customer?.account.id}.${id}`
function load() {
  return loader.run(async () => {
    order.value = await request('customerGetOrder', { id })
  })
}
function pay() {
  return payment.run(
    async () => {
      let key = sessionStorage.getItem(storageKey)
      if (!key) {
        key = createIdempotencyKey()
        sessionStorage.setItem(storageKey, key)
      }
      const paid = await request('payOrder', { id, idempotencyKey: key })
      order.value = paid
      await router.replace(`/customer/orders/${id}`)
    },
    '',
    'pay:' + id,
  )
}
useRefresh(load)
</script>
<template>
  <Feedback :busy="loader.busy.value" :error="loader.error.value" />
  <button v-if="loader.error.value" @click="load">重新查询</button>
  <template v-if="order">
    <h2>{{ order.service.skuName }}</h2>
    <p>{{ label(order.bookingType) }} · {{ order.service.durationMinutes }} 分钟</p>
    <section class="booking-section">
      <p>{{ order.contactName }} · {{ order.contactPhone }}</p>
      <p>{{ order.address.cityName }}{{ order.address.districtName }}{{ order.address.detail }}</p>
      <p>{{ displayTime(order.startTime) }} 至 {{ displayTime(order.endTime) }}</p>
    </section>
    <section class="booking-section">
      <h3>确认金额 ¥{{ order.currentPrice }}</h3>
      <p>
        {{
          useMock
            ? '当前为浏览器原型模拟，不会扣款。'
            : '当前调用真实接口模式的模拟支付接口，不连接真实支付渠道。'
        }}
      </p>
      <p v-if="order.status === 'PENDING_PAYMENT'">
        请在
        {{ displayTime(order.paymentDeadline) }}
        前完成支付（创建后15分钟）。现在退出也会保留待支付订单。
      </p>
      <p v-else>当前订单：{{ label(order.status) }}，请查看最新进度。</p>
    </section>
    <Feedback :busy="payment.busy.value" :error="payment.error.value" />
    <RouterLink :to="`/customer/orders/${id}`">稍后处理 / 查看订单</RouterLink>
    <Teleport to="#customer-actions" defer>
      <div v-if="order.status === 'PENDING_PAYMENT'" class="amount-bar">
        <strong>¥{{ order.currentPrice }}</strong>
        <button class="primary" :disabled="payment.busy.value || loader.busy.value" @click="pay">
          确认模拟支付
        </button>
      </div>
      <RouterLink v-else class="button wide-button" :to="`/customer/orders/${id}`">
        查看预约进度
      </RouterLink>
    </Teleport>
  </template>
</template>
