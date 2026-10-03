<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { request } from '../../api/client'
import { allPages } from '../../api/pagination'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import { sessions } from '../../stores/session'
import Feedback from '../../components/Feedback.vue'
import ModalPanel from '../../components/ModalPanel.vue'
import SceneImages from '../../components/SceneImages.vue'
import WorkerOrderCard from '../../components/WorkerOrderCard.vue'
const task = useTask(),
  claimTask = useTask()
const storageKey = `worker.home.${sessions.worker?.account.id}`
const saved = JSON.parse(sessionStorage.getItem(storageKey) || '{}')
const sort = ref<'DEADLINE' | 'LATEST'>(saved.sort === 'LATEST' ? 'LATEST' : 'DEADLINE'),
  pageNo = ref(Number(saved.pageNo) || 1)
const pool = ref<Schema['OfferPageDTO']>({ list: [], total: 0, pages: 0 }),
  tasks = ref<Schema['OrderVO'][]>([])
const expanded = ref(false),
  selection = ref<Schema['OfferVO']>(),
  latest = ref<Schema['OfferVO']>(),
  unavailable = ref(false),
  changeNotice = ref(''),
  unavailableIds = ref(new Set<string>()),
  newIds = ref(new Set<string>())
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
let knownIds = new Set<string>(),
  timer: ReturnType<typeof setInterval> | undefined,
  syncing = false
function fetchPool() {
  return request('listEligibleOffers', {
    query: { pageNo: pageNo.value, pageSize: 8, sort: sort.value },
  })
}
async function fetchTasks() {
  tasks.value = await allPages((pageNo) =>
    request('workerListOrders', { query: { statuses, pageNo, pageSize: 100 } }),
  )
}
async function loadData() {
  const [offers, all] = await Promise.all([
    fetchPool(),
    allPages((pageNo) =>
      request('listEligibleOffers', { query: { pageNo, pageSize: 100, sort: sort.value } }),
    ),
    fetchTasks(),
  ])
  knownIds = new Set(all.map((o) => o.id))
  pool.value = offers
  unavailableIds.value.clear()
  newIds.value.clear()
  changeNotice.value = ''
  sessionStorage.setItem(storageKey, JSON.stringify({ sort: sort.value, pageNo: pageNo.value }))
}
function load() {
  return task.run(loadData)
}
async function sync() {
  if (syncing || task.busy.value || claimTask.busy.value) return
  syncing = true
  try {
    await task.run(async () => {
      const offers = await allPages((pageNo) =>
        request('listEligibleOffers', { query: { pageNo, pageSize: 100, sort: sort.value } }),
      )
      // 更新已有位置；新订单只提示，确认窗口保留原价快照。
      const map = new Map(offers.map((o) => [o.id, o]))
      pool.value.list = pool.value.list.map((old) => {
        const fresh = map.get(old.id)
        if (!fresh) {
          unavailableIds.value.add(old.id)
          changeNotice.value = '部分订单已被接走、截止或不再符合资格，请刷新抢单池。'
          return old
        }
        unavailableIds.value.delete(old.id)
        if (old.priceVersion !== fresh.priceVersion)
          changeNotice.value = '报价有变化，请查看最新报价后确认。'
        return fresh
      })
      const currentIds = new Set(offers.map((o) => o.id))
      for (const id of newIds.value) if (!currentIds.has(id)) newIds.value.delete(id)
      if (selection.value) {
        latest.value = map.get(selection.value.id)
        unavailable.value = !latest.value
      }
      for (const offer of offers) if (!knownIds.has(offer.id)) newIds.value.add(offer.id)
      await fetchTasks()
    })
  } finally {
    syncing = false
  }
}
function onNotification(event: Event) {
  const notice = (event as CustomEvent<Schema['WsEvent']>).detail
  if (notice.type === 'OFFER_CREATED') newIds.value.add(notice.orderId)
  else changeNotice.value = `${label(notice.type)}，正在同步资格与任务。`
  void sync()
}
function select(offer: Schema['OfferVO']) {
  selection.value = JSON.parse(JSON.stringify(offer))
  latest.value = undefined
  unavailable.value = false
  claimTask.error.value = ''
  claimTask.code.value = ''
}
function closeSelection() {
  if (!claimTask.busy.value) selection.value = undefined
}
async function reviewLatest() {
  await sync()
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
      await loadData()
    },
    '接单成功，已加入已有任务。',
    JSON.stringify(selected),
  )
}
function changeSort() {
  pageNo.value = 1
  void load()
}
function turn(delta: number) {
  pageNo.value += delta
  void load()
}
function closeTasks() {
  expanded.value = false
}
onMounted(() => {
  void load()
  window.addEventListener('business-notification', onNotification)
  window.addEventListener('data-refresh', sync)
  window.addEventListener('focus', sync)
  timer = setInterval(() => void sync(), 30000)
})
onUnmounted(() => {
  clearInterval(timer)
  window.removeEventListener('business-notification', onNotification)
  window.removeEventListener('data-refresh', sync)
  window.removeEventListener('focus', sync)
})
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
    <p v-else>{{ task.busy.value ? '正在查询任务…' : '暂无待处理任务，可浏览下方抢单池。' }}</p>
  </section>
  <Feedback :error="task.error.value" :success="claimTask.success.value" />
  <section aria-label="抢单池">
    <h2>优惠抢单池</h2>
    <p class="muted">
      仅显示符合资格的订单，接单后需完成服务及预留间隔。本页面在线通知，不是手机后台推送。
    </p>
    <label>
      排列顺序
      <select name="sort" v-model="sort" :disabled="task.busy.value" @change="changeSort">
        <option value="DEADLINE">截止时间最近</option>
        <option value="LATEST">最新发布</option>
      </select>
    </label>
    <button class="wide-button" :disabled="task.busy.value" @click="load">
      {{ newIds.size ? `${newIds.size}条新订单，点击更新` : '刷新抢单池' }}
    </button>
    <p v-if="changeNotice" role="status">{{ changeNotice }}</p>
    <article v-for="offer in pool.list" :key="offer.id" class="panel order-card">
      <h2>{{ offer.skuName }}</h2>
      <small>订单 {{ offer.id }}</small>
      <p>{{ displayTime(offer.startTime) }} · {{ offer.durationMinutes }}分钟</p>
      <p>
        广州市{{ offer.districtName }} ·
        <strong>¥{{ offer.currentPrice }}</strong>
      </p>
      <p>接单截止 {{ displayTime(offer.offerDeadline) }}</p>
      <p v-if="offer.sceneImages.length">含{{ offer.sceneImages.length }}张现场图片</p>
      <p v-if="unavailableIds.has(offer.id)">已被接走、截止或资格变化，请刷新。</p>
      <button class="wide-button" :disabled="unavailableIds.has(offer.id)" @click="select(offer)">
        查看详情 / 接单
      </button>
    </article>
    <p v-if="!pool.list.length && !task.busy.value" class="empty">
      暂无符合资格的订单。可检查工作时间和已有安排；技能、调度资格由平台维护。
    </p>
    <div v-if="pool.pages" class="pagination">
      <button :disabled="task.busy.value || pageNo <= 1" @click="turn(-1)">上一页</button>
      <span>{{ pageNo }} / {{ pool.pages }}</span>
      <button :disabled="task.busy.value || pageNo >= pool.pages" @click="turn(1)">下一页</button>
    </div>
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
      该订单已被接走、截止或资格已变化，不能继续接单。请关闭并刷新。
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
