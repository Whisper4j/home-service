<script setup lang="ts">
import { ref } from 'vue'
import { useAutoSync } from '../../composables/useAutoSync'
import { showSuccess } from '../../stores/feedback'
import { useRoute, useRouter } from 'vue-router'
import { createIdempotencyKey, request } from '../../api/client'
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
const uncertain = ref(sessionStorage.getItem(`${storageKey}.uncertain`) === '1')
function load() {
  return loader.run(async () => {
    order.value = await request('customerGetOrder', { id })
  })
}
async function pay() {
  await payment.run(
    async () => {
      let key = sessionStorage.getItem(storageKey)
      if (!key) {
        key = createIdempotencyKey()
        sessionStorage.setItem(storageKey, key)
      }
      const paid = await request('payOrder', { id, idempotencyKey: key })
      uncertain.value = false
      sessionStorage.removeItem(`${storageKey}.uncertain`)
      order.value = paid
      showSuccess('模拟支付成功，正在查询人员安排结果')
      await router.replace(`/customer/result/${id}`)
    },
    '',
    'pay:' + id,
  )
  if (payment.code.value === 'NETWORK_ERROR') {
    uncertain.value = true
    sessionStorage.setItem(`${storageKey}.uncertain`, '1')
  } else if (payment.error.value) {
    uncertain.value = false
    sessionStorage.removeItem(`${storageKey}.uncertain`)
  }
}
useRefresh(load)
const { sync: confirmPayment } = useAutoSync(async (active) => {
  if (!uncertain.value || payment.busy.value) return
  const fresh = await request('customerGetOrder', { id })
  if (!active()) return
  order.value = fresh
  if (fresh.paymentStatus !== 'UNPAID' || fresh.status === 'CANCELLED') {
    uncertain.value = false
    sessionStorage.removeItem(`${storageKey}.uncertain`)
    showSuccess('已确认模拟支付结果')
    await router.replace(`/customer/result/${id}`)
  }
})
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
        {{ '支付由后端模拟支付接口确认，不连接真实支付渠道。' }}
      </p>
      <p v-if="order.status === 'PENDING_PAYMENT'">
        请在
        {{ displayTime(order.paymentDeadline) }}
        前完成支付。现在退出也会保留待支付订单。
      </p>
      <p v-else>当前订单：{{ label(order.status) }}，请查看最新进度。</p>
    </section>
    <Feedback :busy="payment.busy.value" :error="payment.error.value" persistent />
    <p v-if="uncertain" role="status">
      正在确认支付结果。请求超时不代表支付失败，将自动查询；也可使用原幂等键重试，勿重复创建预约。
      <button @click="confirmPayment">查询支付结果</button>
    </p>
    <p v-else-if="payment.error.value">
      支付未成功：{{ payment.error.value }}。请按当前订单状态纠正后重试。
    </p>
    <RouterLink replace :to="`/customer/orders/${id}`">稍后处理 / 查看订单</RouterLink>
    <Teleport to="#customer-actions" defer>
      <div v-if="order.allowedActions.includes('PAY')" class="amount-bar">
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
