<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { request, useMock } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import Feedback from '../../components/CustomerFeedback.vue'
import QuoteDialog from '../../components/QuoteDialog.vue'
import SceneImages from '../../components/SceneImages.vue'
const id = String(useRoute().params.id),
  order = ref<Schema['OrderVO']>(),
  history = ref<Schema['OrderHistoryVO']>(),
  startCode = ref(''),
  reason = ref('')
const loader = useTask(),
  action = useTask(),
  quoteOpen = ref(false)
const review = reactive<Schema['ReviewDTO']>({ score: 5, tags: [], content: '' })
const canCancel = computed(
  () =>
    order.value &&
    ['PENDING_PAYMENT', 'WAITING_DISPATCH', 'WAITING_ACCEPTANCE', 'PENDING_SERVICE'].includes(
      order.value.status,
    ),
)
async function refreshData() {
  const [detail, records] = await Promise.all([
    request('customerGetOrder', { id }),
    request('customerGetOrderHistory', { id }),
  ])
  order.value = detail
  history.value = records
  startCode.value = ''
  if (['PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE'].includes(detail.status))
    startCode.value = (await request('getStartCode', { id })).startCode
}
function load() {
  return loader.run(() => refreshData())
}
function reloadLatest() {
  return loader.run(() => refreshData())
}
function act(operation: 'cancel' | 'confirm' | 'review') {
  return action.run(
    async (key) => {
      const common = { id, idempotencyKey: key }
      if (operation === 'cancel')
        await request('cancelCustomerOrder', { ...common, body: { reason: reason.value } })
      else if (operation === 'confirm') await request('confirmOrder', common)
      else await request('createReview', { ...common, body: { ...review, tags: [...review.tags] } })
      await refreshData()
    },
    '操作成功，已查询最新结果',
    JSON.stringify({ operation, id, reason: reason.value, review }),
  )
}
useRefresh(load)
</script>
<template>
  <Feedback :busy="loader.busy.value" :error="loader.error.value" />
  <button
    class="wide-button"
    :disabled="loader.busy.value || action.busy.value"
    @click="reloadLatest"
  >
    重新查询最新状态
  </button>
  <template v-if="order">
    <section class="booking-section">
      <h2>{{ label(order.status) }}</h2>
      <p>{{ order.service.skuName }} · {{ label(order.bookingType) }}</p>
      <p>
        <strong>¥{{ order.dealPrice || order.currentPrice }}</strong>
        · {{ label(order.paymentStatus) }}
      </p>
      <p>服务人员：{{ order.workerName || '尚未安排' }}</p>
      <p v-if="order.status === 'WAITING_DISPATCH'">
        正在安排人员，请稍候。系统每30秒重试，最晚到{{
          displayTime(order.dispatchDeadline)
        }}；5分钟内未安排成功会取消并全额模拟退款。
      </p>
      <p v-if="order.dispatchStatus === 'SUCCEEDED'">派单成功，人员已安排。</p>
      <p v-if="order.status === 'WAITING_ACCEPTANCE'">
        等待人员接单，当前报价 ¥{{ order.currentPrice }}。截止{{
          displayTime(order.offerDeadline)
        }}，无人接单将取消并全额模拟退款。
      </p>
      <p v-if="order.status === 'PENDING_PAYMENT'">
        待支付，截止{{ displayTime(order.paymentDeadline) }}。创建后15分钟未支付自动取消。
      </p>
      <p v-if="order.status === 'PENDING_CONFIRMATION'">
        人员已提交完成，请确认服务。24小时未处理自动完成：{{
          displayTime(order.confirmationDeadline)
        }}。
      </p>
      <p v-if="order.cancellationReason">取消原因：{{ order.cancellationReason }}</p>
      <p v-if="order.paymentStatus === 'REFUNDED'">已全额模拟退款，金额可在下方记录查看。</p>
    </section>
    <section class="booking-section">
      <h3>预约信息</h3>
      <p>{{ displayTime(order.startTime) }} — {{ displayTime(order.endTime) }}</p>
      <p>{{ order.contactName }} · {{ order.contactPhone }}</p>
      <p>{{ order.address.cityName }}{{ order.address.districtName }}{{ order.address.detail }}</p>
      <p>额外要求：{{ order.remark || '无' }}</p>
      <SceneImages :images="order.sceneImages" role="customer" :order-id="id" />
      <p v-if="!order.sceneImages.length" class="muted">未提供现场图片</p>
      <details>
        <summary>查看已约定的服务范围</summary>
        <p>包含：{{ order.service.included }}</p>
        <p>不包含：{{ order.service.excluded }}</p>
        <p>自备配件：{{ order.service.customerSuppliesParts ? '需要' : '不需要' }}</p>
      </details>
    </section>
    <section v-if="startCode" class="booking-section">
      <h3>服务开始码：{{ startCode }}</h3>
      <p class="muted">请在人员到达后提供，核验正确后才能开始服务。</p>
    </section>
    <Feedback
      :busy="action.busy.value"
      :error="action.error.value"
      :success="action.success.value"
    />
    <button
      v-if="order.status === 'WAITING_ACCEPTANCE'"
      class="wide-button"
      @click="quoteOpen = true"
    >
      调整报价
    </button>
    <QuoteDialog v-if="quoteOpen" :order="order" @close="quoteOpen = false" @saved="load" />
    <section v-if="canCancel" class="booking-section">
      <details>
        <summary>需要取消预约？</summary>
        <form @submit.prevent="act('cancel')">
          <label>
            取消原因
            <input name="reason" v-model.trim="reason" required maxlength="300" />
          </label>
          <p class="muted">已支付部分会全额退回净收款，取消后重新预约可修改地址或时间。</p>
          <button :disabled="action.busy.value">取消预约</button>
        </form>
      </details>
    </section>
    <p v-if="['DEPARTED', 'ARRIVED', 'IN_SERVICE'].includes(order.status)" class="muted">
      服务人员已出发，当前不可由客户直接取消。
    </p>
    <section v-if="order.status === 'COMPLETED' && !order.reviewed" class="booking-section">
      <h3>评价本次服务（每单一次）</h3>
      <form @submit.prevent="act('review')">
        <label>
          评分
          <select name="score" v-model.number="review.score">
            <option v-for="score in 5" :key="score" :value="score">{{ score }} 分</option>
          </select>
        </label>
        <div class="check-group">
          <label
            v-for="tag in ['PUNCTUAL', 'PROFESSIONAL', 'FRIENDLY'] as const"
            :key="tag"
            class="inline"
          >
            <input name="tags" v-model="review.tags" type="checkbox" :value="tag" />
            {{ label(tag) }}
          </label>
        </div>
        <label>
          评价内容
          <textarea name="content" v-model.trim="review.content" maxlength="500" />
        </label>
        <button :disabled="action.busy.value">提交评价</button>
      </form>
    </section>
    <section v-if="history" class="booking-section">
      <h3>支付与退款记录</h3>
      <ul class="record-list">
        <li v-for="item in history.payments" :key="item.id">
          {{ label(item.type) }} · ¥{{ item.amount }}
          <small>{{ displayTime(item.createdAt) }}</small>
        </li>
      </ul>
      <p v-if="!history.payments.length" class="muted">尚无支付记录</p>
      <h3>报价变化</h3>
      <ul class="record-list">
        <li v-for="item in history.priceHistory" :key="item.id">
          ¥{{ item.previousPrice }} → ¥{{ item.newPrice }}
          <small>{{ displayTime(item.createdAt) }}</small>
        </li>
      </ul>
      <p v-if="!history.priceHistory.length" class="muted">尚未调整报价</p>
      <h3>人员安排记录</h3>
      <ul class="record-list">
        <li v-for="item in history.assignments" :key="item.id">
          {{ item.workerName }} · {{ label(item.status) }}
          <small>{{ displayTime(item.assignedAt) }}</small>
        </li>
      </ul>
      <p v-if="history.review">
        评价：{{ history.review.score }} 分 · {{ history.review.tags.map(label).join('、') }} ·
        {{ history.review.content }}
      </p>
    </section>
    <p class="muted">
      订单号 {{ id }}
      <br />
      {{
        useMock ? '本浏览器原型模拟数据，未连接后端服务。' : '真实接口模式；支付仍为项目模拟支付。'
      }}
    </p>
    <Teleport
      v-if="order.status === 'PENDING_PAYMENT' || order.status === 'PENDING_CONFIRMATION'"
      to="#customer-actions"
      defer
    >
      <RouterLink
        v-if="order.status === 'PENDING_PAYMENT'"
        class="button primary wide-button"
        :to="`/customer/pay/${id}`"
      >
        去支付 ¥{{ order.currentPrice }}
      </RouterLink>
      <button
        v-else
        class="primary wide-button"
        :disabled="action.busy.value"
        @click="act('confirm')"
      >
        确认完成
      </button>
    </Teleport>
  </template>
</template>
