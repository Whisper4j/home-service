<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref } from 'vue'
import { request } from '../../api/client'
import { allPages } from '../../api/pagination'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useAutoSync } from '../../composables/useAutoSync'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
import ModalPanel from '../../components/ModalPanel.vue'
import SceneImages from '../../components/SceneImages.vue'
import WorkerOrderCard from '../../components/WorkerOrderCard.vue'
const claimTask = useTask()
const offers = ref<Schema['OfferVO'][]>([]),
  tasks = ref<Schema['OrderVO'][]>([]),
  visibleCount = ref(8)
const visibleOffers = computed(() => offers.value.slice(0, visibleCount.value))
const expanded = ref(false),
  selection = ref<Schema['OfferVO']>(),
  latest = ref<Schema['OfferVO']>(),
  unavailable = ref(false),
  changeNotice = ref('')
const statuses: Schema['OrderStatus'][] = [
  'IN_SERVICE',
  'ARRIVED',
  'DEPARTED',
  'PENDING_SERVICE',
  'PENDING_CONFIRMATION',
]
const quoteChanged = computed(
  () =>
    Boolean(latest.value && latest.value.priceVersion !== selection.value?.priceVersion) ||
    claimTask.code.value === 'PRICE_CHANGED',
)
let initialized = false,
  pointerDown = false,
  releaseTimer: ReturnType<typeof setTimeout> | undefined
let deferred: (() => Promise<void>) | undefined
// A moving offset page is retried if totals or duplicate IDs reveal an intervening mutation.
async function fetchPool() {
  for (let attempt = 0; attempt < 3; attempt++) {
    const first = await request('listEligibleOffers', { query: { pageNo: 1, pageSize: 100 } })
    const rows = [...first.list]
    let stable = true
    for (let pageNo = 2; pageNo <= first.pages; pageNo++) {
      const page = await request('listEligibleOffers', { query: { pageNo, pageSize: 100 } })
      stable &&= page.total === first.total
      rows.push(...page.list)
    }
    const unique = new Map(rows.map((o) => [o.id, o]))
    if (stable && unique.size === first.total) return [...unique.values()]
  }
  throw Error('订单正在变化，稍后自动重新同步')
}
async function apply(
  orders: Schema['OfferVO'][],
  assigned: Schema['OrderVO'][],
  poolConfirmed: boolean,
) {
  const scroll = document.getElementById('worker-scroll')
  const anchors = [...(scroll?.querySelectorAll<HTMLElement>('[data-offer-id]') || [])].filter(
    (el) => el.getBoundingClientRect().bottom > (scroll?.getBoundingClientRect().top || 0),
  )
  const anchor = anchors.find((el) => orders.some((o) => o.id === el.dataset.offerId))
  const top = anchor?.getBoundingClientRect().top
  const previous = new Map(offers.value.map((o) => [o.id, o]))
  const added = orders.filter((o) => !previous.has(o.id)).length
  const removed = offers.value.filter((o) => !orders.some((fresh) => fresh.id === o.id)).length
  const changed = orders.some(
    (o) => previous.has(o.id) && previous.get(o.id)?.priceVersion !== o.priceVersion,
  )
  if (initialized && (added || removed || changed))
    changeNotice.value = [
      added ? `新增${added}单，已更新` : '',
      removed ? `${removed}单已被接走、截止或资格变化，已移出` : '',
      changed ? '报价已更新，确认窗口保留原报价' : '',
    ]
      .filter(Boolean)
      .join('；')
  if (initialized && visibleCount.value >= offers.value.length)
    visibleCount.value = Math.max(visibleCount.value, orders.length)
  offers.value = orders
  tasks.value = assigned
  if (selection.value && poolConfirmed) {
    latest.value = orders.find((o) => o.id === selection.value?.id)
    unavailable.value = !latest.value
  }
  initialized = true
  await nextTick()
  if (scroll && anchor?.isConnected && top !== undefined)
    scroll.scrollTop += anchor.getBoundingClientRect().top - top
}
const { sync, error, loading } = useAutoSync(async (active) => {
  const [poolResult, taskResult] = await Promise.allSettled([
    fetchPool(),
    allPages((pageNo) =>
      request('workerListOrders', { query: { statuses, pageNo, pageSize: 100 } }),
    ),
  ])
  if (!active()) return
  const orders = poolResult.status === 'fulfilled' ? poolResult.value : offers.value
  const assigned = taskResult.status === 'fulfilled' ? taskResult.value : tasks.value
  if (selection.value && poolResult.status === 'fulfilled') {
    latest.value = orders.find((o) => o.id === selection.value?.id)
    unavailable.value = !latest.value
  }
  const update = async () => {
    if (active()) await apply(orders, assigned, poolResult.status === 'fulfilled')
  }
  if (pointerDown) deferred = update
  else {
    deferred = undefined
    await update()
  }
  if (poolResult.status === 'rejected') throw poolResult.reason
  if (taskResult.status === 'rejected') throw taskResult.reason
})
function hold() {
  pointerDown = true
}
function release() {
  clearTimeout(releaseTimer)
  releaseTimer = setTimeout(() => {
    pointerDown = false
    const update = deferred
    deferred = undefined
    void update?.()
  }, 0)
}
onMounted(() => {
  window.addEventListener('pointerdown', hold)
  window.addEventListener('pointerup', release)
  window.addEventListener('pointercancel', release)
  window.addEventListener('blur', release)
})
onUnmounted(() => {
  clearTimeout(releaseTimer)
  deferred = undefined
  window.removeEventListener('pointerdown', hold)
  window.removeEventListener('pointerup', release)
  window.removeEventListener('pointercancel', release)
  window.removeEventListener('blur', release)
})
function select(offer: Schema['OfferVO']) {
  selection.value = structuredClone(JSON.parse(JSON.stringify(offer)))
  latest.value = offers.value.find((o) => o.id === offer.id)
  unavailable.value = !latest.value
  claimTask.error.value = ''
  claimTask.code.value = ''
}
function closeSelection() {
  if (!claimTask.busy.value) selection.value = undefined
}
function reviewLatest() {
  void sync()
}
function acceptLatest() {
  if (latest.value) select(latest.value)
}
async function claim() {
  const selected = selection.value
  if (!selected || quoteChanged.value || unavailable.value) return
  await claimTask.run(
    async (key) => {
      await request('claimOffer', {
        id: selected.id,
        body: { expectedPrice: selected.currentPrice, priceVersion: selected.priceVersion },
        idempotencyKey: key,
      })
      selection.value = undefined
    },
    '接单成功，已加入已有任务。',
    JSON.stringify(selected),
  )
  if (
    ['ORDER_TAKEN', 'OFFER_CLOSED', 'WORKER_INELIGIBLE', 'NOT_FOUND'].includes(claimTask.code.value)
  )
    unavailable.value = true
  await sync()
}
function closeTasks() {
  expanded.value = false
}
function loadMore() {
  visibleCount.value += 8
}
</script>
<template>
  <section class="task-summary" aria-label="已有任务">
    <h2>已有任务</h2>
    <template v-if="tasks.length">
      <RouterLink
        :to="{ path: `/worker/orders/${tasks[0].id}`, query: { returnTo: '/worker/home' } }"
      >
        <strong>{{ label(tasks[0].status) }} · {{ tasks[0].service.skuName }}</strong>
      </RouterLink>
      <p>
        {{ displayTime(tasks[0].startTime) }} ·
        {{ tasks[0].bookingType === 'STANDARD' ? '系统分配' : '自主抢单' }}
      </p>
      <p v-if="tasks[1]" class="muted">
        下一单：{{ tasks[1].service.skuName }} · {{ displayTime(tasks[1].startTime) }}
      </p>
      <button class="wide-button" @click="expanded = true">查看全部{{ tasks.length }}单</button>
    </template>
    <p v-else>{{ loading ? '正在查询任务…' : '暂无待处理任务，可浏览下方抢单池。' }}</p>
  </section>
  <Feedback :success="claimTask.success.value" />
  <p v-if="error" role="status">
    {{ error }}
    <button @click="sync">重试连接</button>
  </p>
  <section aria-label="抢单池">
    <h2>优惠抢单池</h2>
    <p class="muted">
      仅显示符合资格的订单，接单后需完成服务及预留间隔。本页面在线通知，不是手机后台推送。
    </p>
    <p v-if="changeNotice" role="status">{{ changeNotice }}</p>
    <article
      v-for="offer in visibleOffers"
      :key="offer.id"
      :data-offer-id="offer.id"
      class="panel order-card"
    >
      <h2>{{ offer.skuName }}</h2>
      <small>订单 {{ offer.id }}</small>
      <p>{{ displayTime(offer.startTime) }} · {{ offer.durationMinutes }}分钟</p>
      <p>
        {{ offer.cityName }}{{ offer.districtName }} ·
        <strong>¥{{ offer.currentPrice }}</strong>
      </p>
      <p>接单截止 {{ displayTime(offer.offerDeadline) }}</p>
      <p v-if="offer.sceneImages.length">含{{ offer.sceneImages.length }}张现场图片</p>
      <button class="wide-button" @click="select(offer)">查看详情 / 接单</button>
    </article>
    <p v-if="!offers.length && !loading" class="empty">
      暂无符合资格的订单。可检查工作时间和已有安排；技能、调度资格由平台维护。
    </p>
    <button v-if="visibleCount < offers.length" class="wide-button" @click="loadMore">
      继续查看较新订单（剩余{{ offers.length - visibleCount }}单）
    </button>
  </section>
  <ModalPanel v-if="expanded" title="全部已有任务" expanded @close="closeTasks">
    <WorkerOrderCard
      v-for="order in tasks"
      :key="order.id"
      :order="order"
      return-to="/worker/home"
    />
    <p v-if="!tasks.length">暂无任务</p>
  </ModalPanel>
  <ModalPanel
    v-if="selection"
    title="确认接单"
    :busy="claimTask.busy.value"
    @close="closeSelection"
  >
    <h3>{{ selection.skuName }}</h3>
    <p>本次确认报价 ¥{{ selection.currentPrice }}</p>
    <p>
      {{ displayTime(selection.startTime) }} · {{ selection.durationMinutes }}分钟 ·
      {{ selection.districtName }}
    </p>
    <p>{{ selection.description }}</p>
    <p>包含：{{ selection.included }}</p>
    <p>排除：{{ selection.excluded }}</p>
    <p>服务后预留到 {{ displayTime(selection.bufferEndTime) }}</p>
    <SceneImages :images="selection.sceneImages" role="worker" :order-id="selection.id" />
    <p class="muted">
      图片仅供了解现场，不扩大范围，不允许现场议价。接单前隐藏完整门牌、电话及开始码。
    </p>
    <Feedback :error="claimTask.error.value" persistent />
    <p v-if="unavailable" role="alert">
      该订单已被接走、截止或资格已变化，不能继续接单。请关闭后查看其他订单。
    </p>
    <section v-if="quoteChanged" class="panel" role="alert">
      <p>
        原报价 ¥{{ selection.currentPrice }}，最新报价 ¥{{
          latest?.currentPrice || claimTask.details.value.currentPrice
        }}，请重新确认。
      </p>
      <button @click="reviewLatest">查看最新报价</button>
      <button v-if="latest" @click="acceptLatest">我已查看，重新确认此报价</button>
    </section>
    <template #actions>
      <button
        class="wide-button"
        :disabled="claimTask.busy.value || quoteChanged || unavailable"
        @click="claim"
      >
        按此价格确认接单
      </button>
    </template>
  </ModalPanel>
</template>
