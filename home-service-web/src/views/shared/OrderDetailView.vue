<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { request, useMock } from '../../api/client'
import type { RolePath, Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
import SceneImages from '../../components/SceneImages.vue'
const props = defineProps<{ role: RolePath }>(),
  route = useRoute()
const order = ref<Schema['OrderVO']>(),
  history = ref<Schema['OrderHistoryVO']>(),
  startCode = ref(''),
  enteredCode = ref(''),
  reason = ref('')
const price = reactive({
  newPrice: '',
  expectedPrice: '',
  priceVersion: 1,
  confirmSimulatedPayment: false,
})
const review = reactive<Schema['ReviewDTO']>({ score: 5, tags: [], content: '' })
const { busy, error, success, run } = useTask()
const id = String(route.params.id)
let priceInitialized = false
const canCancel = computed(
  () =>
    order.value &&
    (props.role === 'admin'
      ? !['COMPLETED', 'CANCELLED'].includes(order.value.status)
      : props.role === 'customer' &&
        ['PENDING_PAYMENT', 'WAITING_DISPATCH', 'WAITING_ACCEPTANCE', 'PENDING_SERVICE'].includes(
          order.value.status,
        )),
)
async function refreshData(resetPrice = false) {
  const [detail, records] = await Promise.all([
    request(
      ({ customer: 'customerGetOrder', worker: 'workerGetOrder', admin: 'adminGetOrder' } as const)[
        props.role
      ],
      { id },
    ),
    request(
      (
        {
          customer: 'customerGetOrderHistory',
          worker: 'workerGetOrderHistory',
          admin: 'adminGetOrderHistory',
        } as const
      )[props.role],
      { id },
    ),
  ])
  order.value = detail
  history.value = records
  if (!priceInitialized || resetPrice) {
    Object.assign(price, {
      newPrice: detail.currentPrice,
      expectedPrice: detail.currentPrice,
      priceVersion: detail.priceVersion,
      confirmSimulatedPayment: false,
    })
    priceInitialized = true
  }
  startCode.value = ''
  if (
    props.role === 'customer' &&
    ['PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE'].includes(detail.status)
  )
    startCode.value = (await request('getStartCode', { id })).startCode
}
function load() {
  return run(() => refreshData())
}
function reloadLatest() {
  return run(() => refreshData(true))
}
function act(
  action:
    'pay' | 'cancel' | 'price' | 'depart' | 'arrive' | 'start' | 'finish' | 'confirm' | 'review',
) {
  void run(
    async (key) => {
      const common = { id, idempotencyKey: key }
      switch (action) {
        case 'pay':
          await request('payOrder', common)
          break
        case 'cancel':
          await request(props.role === 'admin' ? 'cancelAdminOrder' : 'cancelCustomerOrder', {
            ...common,
            body: { reason: reason.value },
          })
          break
        case 'price':
          await request('changeOffer', { ...common, body: { ...price } })
          break
        case 'depart':
          await request('departOrder', common)
          break
        case 'arrive':
          await request('arriveOrder', common)
          break
        case 'start':
          await request('startOrder', { ...common, body: { startCode: enteredCode.value } })
          break
        case 'finish':
          await request('finishOrder', common)
          break
        case 'confirm':
          await request('confirmOrder', common)
          break
        case 'review':
          await request('createReview', { ...common, body: { ...review, tags: [...review.tags] } })
          break
      }
      await refreshData(true)
    },
    '操作成功，已查询最新结果',
    JSON.stringify({
      action,
      id,
      reason: reason.value,
      price,
      enteredCode: enteredCode.value,
      review,
    }),
  )
}
async function advanceToStart() {
  const demo = await import('../../mock/transport')
  await demo.advanceClock(
    Math.max(0, (Date.parse(order.value!.startTime) - demo.mockNow()) / 60_000),
  )
  window.dispatchEvent(new Event('data-refresh'))
}
useRefresh(load)
</script>
<template>
  <div class="actions">
    <RouterLink :to="`/${role}/orders`">返回订单列表</RouterLink>
    <button :disabled="busy" @click="reloadLatest">重新查询最新状态</button>
  </div>
  <h1>订单详情 {{ id }}</h1>
  <Feedback :error="error" :success="success" :busy="busy" />
  <template v-if="order">
    <div class="grid">
      <section class="panel">
        <h2>{{ order.service.skuName }}</h2>
        <dl>
          <dt>预约方式</dt>
          <dd>{{ label(order.bookingType) }}</dd>
          <dt>履约状态</dt>
          <dd>
            <strong>{{ label(order.status) }}</strong>
          </dd>
          <dt>支付状态</dt>
          <dd>{{ label(order.paymentStatus) }}</dd>
          <dt>调度状态</dt>
          <dd>{{ label(order.dispatchStatus) }}</dd>
          <dt>当前报价</dt>
          <dd>¥{{ order.currentPrice }} · 版本 {{ order.priceVersion }}</dd>
          <dt>成交价</dt>
          <dd>{{ order.dealPrice ? `¥${order.dealPrice}` : '尚未成交' }}</dd>
          <dt>服务人员</dt>
          <dd>{{ order.workerName || '尚未分配' }}</dd>
          <dt>预约开始</dt>
          <dd>{{ displayTime(order.startTime) }}</dd>
          <dt>预计结束</dt>
          <dd>{{ displayTime(order.endTime) }}</dd>
          <dt>缓冲结束</dt>
          <dd>{{ displayTime(order.bufferEndTime) }}</dd>
          <dt>支付截止</dt>
          <dd>{{ displayTime(order.paymentDeadline) }}</dd>
          <dt v-if="order.offerDeadline">抢单截止</dt>
          <dd v-if="order.offerDeadline">{{ displayTime(order.offerDeadline) }}</dd>
          <dt v-if="order.dispatchDeadline">派单等待截止</dt>
          <dd v-if="order.dispatchDeadline">{{ displayTime(order.dispatchDeadline) }}</dd>
          <dt v-if="order.confirmationDeadline">自动确认时间</dt>
          <dd v-if="order.confirmationDeadline">{{ displayTime(order.confirmationDeadline) }}</dd>
        </dl>
      </section>
      <section class="panel">
        <h2>服务与地址快照</h2>
        <p>本次联系人：{{ order.contactName }} · {{ order.contactPhone }}</p>
        <p>
          {{ order.address.provinceName }}{{ order.address.cityName }}{{ order.address.districtName
          }}{{ order.address.detail }}
        </p>
        <p>服务说明：{{ order.service.description }}</p>
        <p>包含：{{ order.service.included }}</p>
        <p>排除：{{ order.service.excluded }}</p>
        <p>客户自备配件：{{ order.service.customerSuppliesParts ? '需要' : '不需要' }}</p>
        <p>备注：{{ order.remark || '无' }}</p>
        <SceneImages :images="order.sceneImages" :role="role" :order-id="id" />
        <p v-if="order.cancellationReason">取消原因：{{ order.cancellationReason }}</p>
        <p v-if="startCode">
          <strong>服务开始码：{{ startCode }}</strong>
          <br />
          请在服务人员到达后提供。开始码不作为定位证明。
        </p>
        <p v-if="order.status === 'WAITING_DISPATCH'">
          系统每30秒尝试调度，最长等待5分钟；超时取消并全额模拟退款。
        </p>
        <p v-if="order.status === 'WAITING_ACCEPTANCE'">
          接单截止前可调整报价；无人接单则取消退款，不会自动加价或转标准。
        </p>
      </section>
    </div>
    <section class="panel">
      <h2>当前可执行操作</h2>
      <div class="actions">
        <button
          v-if="role === 'customer' && order.status === 'PENDING_PAYMENT'"
          :disabled="busy"
          @click="act('pay')"
        >
          模拟支付 ¥{{ order.currentPrice }}
        </button>
        <button
          v-if="role === 'customer' && order.status === 'PENDING_CONFIRMATION'"
          :disabled="busy"
          @click="act('confirm')"
        >
          确认完成
        </button>
        <button
          v-if="role === 'worker' && order.status === 'PENDING_SERVICE'"
          :disabled="busy"
          @click="act('depart')"
        >
          确认出发
        </button>
        <button
          v-if="role === 'worker' && order.status === 'DEPARTED'"
          :disabled="busy"
          @click="act('arrive')"
        >
          确认到达
        </button>
        <button
          v-if="role === 'worker' && order.status === 'IN_SERVICE'"
          :disabled="busy"
          @click="act('finish')"
        >
          提交服务完成
        </button>
        <button
          v-if="useMock && ['PENDING_SERVICE', 'DEPARTED', 'ARRIVED'].includes(order.status)"
          @click="advanceToStart"
        >
          演示：推进至预约开始
        </button>
      </div>
      <form
        v-if="role === 'worker' && order.status === 'ARRIVED'"
        class="filters"
        @submit.prevent="act('start')"
      >
        <label>
          客户提供的六位开始码
          <input
            v-model="enteredCode"
            required
            pattern="[0-9]{6}"
            maxlength="6"
            inputmode="numeric"
          />
        </label>
        <button :disabled="busy">验证并开始服务</button>
      </form>
      <form
        v-if="role === 'customer' && order.status === 'WAITING_ACCEPTANCE'"
        @submit.prevent="act('price')"
      >
        <h3>调整优惠报价</h3>
        <p>
          当前确认快照：¥{{ price.expectedPrice }} / 版本 {{ price.priceVersion }}。范围 [{{
            order.service.minimumOfferPrice
          }}, {{ order.service.standardPrice }})，步长5元。
        </p>
        <label>
          新报价
          <input v-model="price.newPrice" required pattern="[0-9]+\.[0-9]{2}" inputmode="decimal" />
        </label>
        <label class="inline">
          <input v-model="price.confirmSimulatedPayment" type="checkbox" />
          如果加价，我确认模拟支付差额；降价自动记录部分退款
        </label>
        <button :disabled="busy">提交报价调整</button>
      </form>
      <form v-if="canCancel" class="filters" @submit.prevent="act('cancel')">
        <label>
          {{ role === 'admin' ? '异常取消原因（必填）' : '取消原因' }}
          <input v-model.trim="reason" required maxlength="300" />
        </label>
        <button :disabled="busy">{{ role === 'admin' ? '异常取消并退款' : '取消预约' }}</button>
        <span>已支付部分将全额退回净收款，释放服务及缓冲槽。</span>
      </form>
      <p v-if="['DEPARTED', 'ARRIVED', 'IN_SERVICE'].includes(order.status) && role === 'customer'">
        服务人员已出发，当前不可由客户直接取消。
      </p>
      <form
        v-if="role === 'customer' && order.status === 'COMPLETED' && !order.reviewed"
        @submit.prevent="act('review')"
      >
        <h3>评价本次服务（每单一次）</h3>
        <label>
          评分
          <select v-model.number="review.score">
            <option v-for="score in 5" :key="score" :value="score">{{ score }} 分</option>
          </select>
        </label>
        <div class="check-group">
          <label
            v-for="tag in ['PUNCTUAL', 'PROFESSIONAL', 'FRIENDLY'] as const"
            :key="tag"
            class="inline"
          >
            <input v-model="review.tags" type="checkbox" :value="tag" />
            {{ label(tag) }}
          </label>
        </div>
        <label>
          评价内容
          <textarea v-model.trim="review.content" maxlength="500" />
        </label>
        <button :disabled="busy">提交评价</button>
      </form>
      <p v-if="['COMPLETED', 'CANCELLED'].includes(order.status)">订单已结束，历史记录保留。</p>
    </section>
    <section v-if="history" class="panel">
      <h2>流水与历史</h2>
      <h3>支付 / 退款流水</h3>
      <div class="table-scroll">
        <table>
          <thead>
            <tr>
              <th>业务号</th>
              <th>类型</th>
              <th>金额</th>
              <th>时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="payment in history.payments" :key="payment.id">
              <td>{{ payment.businessNo }}</td>
              <td>{{ label(payment.type) }}</td>
              <td>¥{{ payment.amount }}</td>
              <td>{{ displayTime(payment.createdAt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-if="!history.payments.length">暂无流水。</p>
      <h3>报价历史</h3>
      <ul>
        <li v-for="change in history.priceHistory" :key="change.id">
          {{ displayTime(change.createdAt) }} · ¥{{ change.previousPrice }} → ¥{{
            change.newPrice
          }}
          · 版本 {{ change.priceVersion }}
        </li>
      </ul>
      <p v-if="!history.priceHistory.length">尚未调整报价。</p>
      <h3>分配记录</h3>
      <ul>
        <li v-for="assignment in history.assignments" :key="assignment.id">
          {{ assignment.workerName }} · {{ label(assignment.bookingType) }} ·
          {{ label(assignment.status) }} · {{ displayTime(assignment.assignedAt) }}
        </li>
      </ul>
      <p v-if="!history.assignments.length">尚无生效分配。</p>
      <p v-if="history.review">
        评价：{{ history.review.score }} 分 · {{ history.review.tags.map(label).join('、') }} ·
        {{ history.review.content }}
      </p>
    </section>
  </template>
</template>
