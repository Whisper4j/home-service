<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { sessions } from '../../stores/session'
import { request, useMock } from '../../api/client'
import type { Schema } from '../../api/types'
import {
  dateOf,
  displayTime,
  fromLocalInput,
  localInput,
  tomorrowMorning,
} from '../../utils/format'
import { label } from '../../utils/labels'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
const schedule = reactive<Schema['ScheduleDTO']>({
  intervals: [{ start: '08:00', end: '22:00' }],
  restWeekdays: [],
})
const leave = reactive({ startTime: '', endTime: '', reason: '' }),
  date = ref(dateOf(Date.now()))
const slots = ref<Schema['SlotVO'][]>([]),
  leaves = ref<Schema['LeavePageDTO']>({ list: [], total: 0, pages: 0 }),
  profile = ref<Schema['WorkerVO']>(),
  calendar = ref<Schema['WorkerCalendarVO']>(),
  month = ref(date.value.slice(0, 7))
const page = reactive({ pageNo: 1, pageSize: 5 }),
  initialized = ref(false)
const { busy, error, success, details, run } = useTask()
const storageKey = `worker.schedule.${sessions.worker?.account.id}`
let draft:
  { schedule: Schema['ScheduleDTO']; leave: typeof leave; date: string; month: string } | undefined
try {
  draft = JSON.parse(sessionStorage.getItem(storageKey) || 'null') || undefined
} catch {
  /* 损坏草稿由接口数据恢复 */
}
watch(
  [schedule, leave, date, month],
  () => {
    if (initialized.value)
      sessionStorage.setItem(
        storageKey,
        JSON.stringify({ schedule, leave, date: date.value, month: month.value }),
      )
  },
  { deep: true },
)
async function refreshData() {
  if (!initialized.value) {
    const now = useMock ? (await import('../../mock/transport')).mockNow() : Date.now()
    date.value = dateOf(now)
    month.value = date.value.slice(0, 7)
    leave.startTime = localInput(tomorrowMorning(now))
    leave.endTime = localInput(tomorrowMorning(now) + 3_600_000)
    if (draft) {
      Object.assign(leave, draft.leave)
      if (draft.date >= dateOf(now) && draft.date < dateOf(now + 30 * 86400000)) {
        date.value = draft.date
        month.value = draft.month
      }
    }
  }
  const [template, day, history, person] = await Promise.all([
    request('getSchedule'),
    request('getWorkerCalendar', { query: { month: month.value } }),
    request('listLeaves', { query: page }),
    request('getWorkerProfile'),
  ])
  if (!initialized.value) {
    Object.assign(schedule, {
      intervals: template.configured ? template.intervals : [{ start: '08:00', end: '22:00' }],
      restWeekdays: template.restWeekdays,
    })
    if (draft) Object.assign(schedule, draft.schedule)
    initialized.value = true
  }
  calendar.value = day
  if (!day.days.some((d) => d.date === date.value)) date.value = day.days[0]?.date || ''
  slots.value = day.days.find((d) => d.date === date.value)?.segments || []
  leaves.value = history
  profile.value = person
}
function load() {
  return run(refreshData)
}
function save() {
  void run(
    async (key) => {
      await request('updateSchedule', {
        body: {
          intervals: schedule.intervals.map((i) => ({ ...i })),
          restWeekdays: [...schedule.restWeekdays],
        },
        idempotencyKey: key,
      })
      await refreshData()
    },
    '排班已保存，未影响已有订单',
    JSON.stringify(schedule),
  )
}
function submitLeave() {
  void run(
    async (key) => {
      await request('createLeave', {
        body: {
          startTime: fromLocalInput(leave.startTime),
          endTime: fromLocalInput(leave.endTime),
          reason: leave.reason,
        },
        idempotencyKey: key,
      })
      await refreshData()
    },
    '请假已生效',
    JSON.stringify(leave),
  )
}
function cancel(id: string) {
  void run(
    async (key) => {
      await request('cancelLeave', { id, idempotencyKey: key })
      await refreshData()
    },
    '请假已撤销',
    id,
  )
}
useRefresh(load)
function changePage(value: number) {
  page.pageNo = value
  void load()
}
function resizePage(value: number) {
  page.pageSize = value
  page.pageNo = 1
  void load()
}
function setWeekdaySchedule() {
  schedule.intervals = [{ start: '08:00', end: '22:00' }]
  schedule.restWeekdays = [6, 7]
}
const dayLabels: Record<string, string> = {
  UNCONFIGURED: '未设置',
  REST: '休息',
  AVAILABLE: '工作时间内空闲',
  ARRANGED: '有安排',
  LEAVE: '请假',
}
const cells = computed(() => {
  const start = new Date(`${month.value}-01T00:00:00+08:00`)
  const prefix = (new Date(start.getTime() + 8 * 3600000).getUTCDay() + 6) % 7
  const count = new Date(
    Number(month.value.slice(0, 4)),
    Number(month.value.slice(5, 7)),
    0,
  ).getDate()
  return [
    ...Array.from({ length: prefix }, () => ''),
    ...Array.from({ length: count }, (_, i) => `${month.value}-${String(i + 1).padStart(2, '0')}`),
  ]
})
function selectDate(value: string) {
  date.value = value
  slots.value = calendar.value?.days.find((d) => d.date === value)?.segments || []
}
function dateStatus(value: string) {
  return calendar.value?.days.find((d) => d.date === value)?.status
}
function previousMonth(delta: number) {
  const value = new Date(`${month.value}-15T00:00:00+08:00`)
  value.setUTCMonth(value.getUTCMonth() + delta)
  month.value = dateOf(value.getTime()).slice(0, 7)
  void load()
}
function canMonth(delta: number) {
  const value = new Date(`${month.value}-15T00:00:00+08:00`)
  value.setUTCMonth(value.getUTCMonth() + delta)
  const next = dateOf(value.getTime()).slice(0, 7)
  return Boolean(
    calendar.value &&
    next >= calendar.value.from.slice(0, 7) &&
    next <= calendar.value.to.slice(0, 7),
  )
}
</script>
<template>
  <h1>排班与请假</h1>
  <p v-if="profile">
    {{ profile.displayName }} ·
    {{ profile.dispatchEnabled ? '允许新分配' : '暂停新分配，已有订单继续履约' }}
  </p>
  <Feedback :error="error" :success="success" :busy="busy" />
  <p v-for="id in details.conflictingOrderIds || []" :key="id">
    <RouterLink :to="{ path: `/worker/orders/${id}`, query: { returnTo: '/worker/schedule' } }">
      查看冲突订单 {{ id }}
    </RouterLink>
    ；请假不能取消已接订单。
  </p>
  <section class="panel" aria-label="工作月历">
    <h2>工作月历</h2>
    <div class="pagination">
      <button :disabled="busy || !canMonth(-1)" @click="previousMonth(-1)">上月</button>
      <strong>{{ month }}</strong>
      <button :disabled="busy || !canMonth(1)" @click="previousMonth(1)">下月</button>
    </div>
    <div class="calendar-grid">
      <span v-for="name in ['一', '二', '三', '四', '五', '六', '日']" :key="name">{{ name }}</span>
      <template v-for="(cell, index) in cells" :key="index">
        <button
          v-if="cell"
          :disabled="!dateStatus(cell)"
          :aria-pressed="date === cell"
          :aria-label="cell + ' ' + (dayLabels[dateStatus(cell) || ''] || '窗口外')"
          @click="selectDate(cell)"
        >
          {{ Number(cell.slice(-2)) }}
          <small>
            {{
              dateStatus(cell) === 'AVAILABLE' ? '空闲' : dayLabels[dateStatus(cell) || ''] || '—'
            }}
          </small>
        </button>
        <span v-else />
      </template>
    </div>
    <p class="muted">
      仅展示未来30天；这不扩大客户预约窗口。空闲仅指工作时间内，剩余时段不保证容纳任意服务。
    </p>
    <p v-if="calendar && !calendar.schedule.configured">
      尚未设置工作时间，请在下方设置统一工作区间。
    </p>
    <p v-else-if="calendar">
      工作范围：{{
        calendar.schedule.intervals.map((i) => `${i.start}—${i.end}`).join('、')
      }}；固定休息日：{{
        calendar.schedule.restWeekdays
          .map((n) => `周${['一', '二', '三', '四', '五', '六', '日'][n - 1]}`)
          .join('、') || '无'
      }}
    </p>
    <h3>{{ date }} 安排</h3>
    <ul class="record-list">
      <li v-for="slot in slots" :key="slot.startTime">
        {{ slot.startTime.slice(11, 16) }}—{{ slot.endTime.slice(11, 16) }} ·
        {{
          slot.status === 'AVAILABLE'
            ? '工作时间内空闲 / 可接时段'
            : slot.status === 'BUFFER'
              ? '服务后预留间隔（不可接单）'
              : label(slot.status)
        }}
        <RouterLink
          v-if="slot.orderId"
          :to="{ path: `/worker/orders/${slot.orderId}`, query: { returnTo: '/worker/schedule' } }"
        >
          查看订单 {{ slot.orderId }}
        </RouterLink>
      </li>
    </ul>
  </section>
  <div class="grid">
    <section class="panel">
      <h2>统一每日工作区间</h2>
      <p>所有非休息日使用同一套区间，平台开放08:00—22:00；不得与已有服务槽或缓冲槽冲突。</p>
      <form @submit.prevent="save">
        <div v-for="(interval, index) in schedule.intervals" :key="index" class="actions">
          <label>
            开始
            <input
              name="start"
              v-model="interval.start"
              type="time"
              min="08:00"
              max="22:00"
              step="1800"
              required
            />
          </label>
          <label>
            结束
            <input
              name="end"
              v-model="interval.end"
              type="time"
              min="08:00"
              max="22:00"
              step="1800"
              required
            />
          </label>
          <button
            type="button"
            :disabled="schedule.intervals.length === 1"
            @click="schedule.intervals.splice(index, 1)"
          >
            移除
          </button>
        </div>
        <div class="actions">
          <button type="button" @click="schedule.intervals.push({ start: '13:00', end: '22:00' })">
            添加区间
          </button>
          <button type="button" @click="setWeekdaySchedule">一键工作日08:00—22:00</button>
        </div>
        <p>每周固定休息日</p>
        <div class="check-group">
          <label
            v-for="(name, i) in ['一', '二', '三', '四', '五', '六', '日']"
            :key="i"
            class="inline"
          >
            <input
              name="restWeekdays"
              v-model="schedule.restWeekdays"
              type="checkbox"
              :value="i + 1"
            />
            周{{ name }}
          </label>
        </div>
        <button :disabled="busy">保存排班</button>
      </form>
    </section>
    <section class="panel">
      <h2>临时请假</h2>
      <p>至少提前2小时，按30分钟对齐；已分配订单的服务和缓冲时间不能请假。</p>
      <form @submit.prevent="submitLeave">
        <label>
          开始（北京时间）
          <input
            name="startTime"
            v-model="leave.startTime"
            type="datetime-local"
            step="1800"
            required
          />
        </label>
        <label>
          结束（北京时间）
          <input
            name="endTime"
            v-model="leave.endTime"
            type="datetime-local"
            step="1800"
            required
          />
        </label>
        <label>
          原因
          <input name="reason" v-model.trim="leave.reason" required maxlength="300" />
        </label>
        <button :disabled="busy">提交请假</button>
      </form>
    </section>
  </div>
  <section class="panel">
    <h2>请假记录</h2>
    <article v-for="record in leaves.list" :key="record.id" class="panel">
      <p>{{ displayTime(record.startTime) }} — {{ displayTime(record.endTime) }}</p>
      <p>{{ record.reason }} · {{ label(record.status) }}</p>
      <button v-if="record.status === 'ACTIVE'" :disabled="busy" @click="cancel(record.id)">
        撤销请假
      </button>
    </article>
    <p v-if="!leaves.list.length">暂无请假记录</p>
    <Pagination
      v-bind="page"
      :total="leaves.total"
      :pages="leaves.pages"
      @change="changePage"
      @resize="resizePage"
    />
  </section>
</template>
