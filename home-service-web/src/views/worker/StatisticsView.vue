<script setup lang="ts">
import { ref } from 'vue'
import { request, useMock } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import Feedback from '../../components/Feedback.vue'
const stats = ref<Schema['WorkerStatisticsVO']>(),
  task = useTask()
function load() {
  return task.run(async () => {
    stats.value = await request('getWorkerStatistics')
  })
}
useRefresh(load)
</script>
<template>
  <Feedback :error="task.error.value" :busy="task.busy.value" />
  <button @click="load">刷新服务数据</button>
  <template v-if="stats">
    <section class="panel">
      <h2>今日待服务 {{ stats.todayPendingCount }} 单</h2>
      <p>{{ stats.today }}预约开始且当前为待服务的订单</p>
    </section>
    <section class="panel">
      <h2>本月已完成 {{ stats.monthCompletedCount }} 单</h2>
      <p>{{ stats.month }} · 预约服务时长 {{ stats.monthBookedMinutes }} 分钟</p>
    </section>
    <section class="panel">
      <h2>累计已完成 {{ stats.totalCompletedCount }} 单</h2>
      <p>预约服务时长 {{ stats.totalBookedMinutes }} 分钟</p>
    </section>
    <p>
      统计截至
      {{
        displayTime(stats.asOf)
      }}，使用北京时间。已完成以客户确认或24小时自动确认的完成时间归属月份。待客户确认不计入已完成。
    </p>
    <p>
      时长为已完成订单约定的预约服务时长，不代表实际工时；订单金额不是人员收入。本项目不提供结算。
    </p>
    <p v-if="useMock" class="muted">当前为浏览器 Mock 完整演示数据聚合，非真实后端统计。</p>
  </template>
</template>
