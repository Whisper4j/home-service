<script setup lang="ts">
import { reactive, ref } from 'vue'
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
  profile = ref<Schema['WorkerVO']>()
const page = reactive({ pageNo: 1, pageSize: 5 }),
  initialized = ref(false)
const { busy, error, success, run } = useTask()
async function refreshData() {
  if (!initialized.value) {
    const now = useMock ? (await import('../../mock/transport')).mockNow() : Date.now()
    date.value = dateOf(now)
    leave.startTime = localInput(tomorrowMorning(now))
    leave.endTime = localInput(tomorrowMorning(now) + 3_600_000)
  }
  const [template, day, history, person] = await Promise.all([
    request('getSchedule'),
    request('listWorkerSlots', { query: { date: date.value } }),
    request('listLeaves', { query: page }),
    request('getWorkerProfile'),
  ])
  if (!initialized.value) {
    Object.assign(schedule, {
      intervals: template.configured ? template.intervals : [{ start: '08:00', end: '22:00' }],
      restWeekdays: template.restWeekdays,
    })
    initialized.value = true
  }
  slots.value = day
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
</script>
<template>
  <h1>排班与请假</h1>
  <p v-if="profile">
    {{ profile.displayName }} ·
    {{ profile.dispatchEnabled ? '允许新分配' : '暂停新分配，已有订单继续履约' }}
  </p>
  <Feedback :error="error" :success="success" :busy="busy" />
  <div class="grid">
    <section class="panel">
      <h2>统一每日工作区间</h2>
      <p>所有非休息日使用同一套区间，平台开放08:00—22:00；不得与已有服务槽或缓冲槽冲突。</p>
      <form @submit.prevent="save">
        <div v-for="(interval, index) in schedule.intervals" :key="index" class="actions">
          <label>
            开始
            <input
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
            <input v-model="schedule.restWeekdays" type="checkbox" :value="i + 1" />
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
          <input v-model="leave.startTime" type="datetime-local" step="1800" required />
        </label>
        <label>
          结束（北京时间）
          <input v-model="leave.endTime" type="datetime-local" step="1800" required />
        </label>
        <label>
          原因
          <input v-model.trim="leave.reason" required maxlength="300" />
        </label>
        <button :disabled="busy">提交请假</button>
      </form>
    </section>
  </div>
  <section class="panel">
    <h2>未来30天时间槽</h2>
    <form class="filters" @submit.prevent="load">
      <label>
        查看日期
        <input v-model="date" type="date" required />
      </label>
      <button :disabled="busy">查询时间槽</button>
    </form>
    <div class="slots">
      <div
        v-for="slot in slots"
        :key="slot.startTime"
        class="slot"
        :class="{ occupied: slot.status !== 'AVAILABLE' }"
      >
        {{ slot.startTime.slice(11, 16) }}—{{ slot.endTime.slice(11, 16) }}
        <br />
        {{ label(slot.status) }} {{ slot.bookingType ? label(slot.bookingType) : '' }}
        <br />
        <RouterLink v-if="slot.orderId" :to="`/worker/orders/${slot.orderId}`">
          订单 {{ slot.orderId }}
        </RouterLink>
      </div>
    </div>
  </section>
  <section class="panel">
    <h2>请假记录</h2>
    <div class="table-scroll">
      <table>
        <thead>
          <tr>
            <th>开始 / 结束</th>
            <th>原因</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in leaves.list" :key="record.id">
            <td>
              {{ displayTime(record.startTime) }}
              <br />
              {{ displayTime(record.endTime) }}
            </td>
            <td>{{ record.reason }}</td>
            <td>{{ label(record.status) }}</td>
            <td>
              <button v-if="record.status === 'ACTIVE'" :disabled="busy" @click="cancel(record.id)">
                撤销请假
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <Pagination
      v-bind="page"
      :total="leaves.total"
      :pages="leaves.pages"
      @change="changePage"
      @resize="resizePage"
    />
  </section>
</template>
