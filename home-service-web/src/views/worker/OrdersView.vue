<script setup lang="ts">
import { ref, watch } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { sessions } from '../../stores/session'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import OrderGroups from '../../components/OrderGroups.vue'
import Feedback from '../../components/Feedback.vue'
import WorkerOrderCard from '../../components/WorkerOrderCard.vue'
const groups: { name: string; statuses?: Schema['OrderStatus'][] }[] = [
  { name: '全部' },
  { name: '进行中', statuses: ['IN_SERVICE', 'ARRIVED', 'DEPARTED'] },
  { name: '待服务', statuses: ['PENDING_SERVICE'] },
  { name: '待确认', statuses: ['PENDING_CONFIRMATION'] },
  { name: '已结束', statuses: ['COMPLETED', 'CANCELLED'] },
]
const key = `worker.orders.${sessions.worker?.account.id}`,
  saved = JSON.parse(sessionStorage.getItem(key) || '{}')
const group = ref(Number(saved.group) || 0),
  pageNo = ref(Number(saved.pageNo) || 1),
  page = ref<Schema['OrderPageDTO']>({ list: [], total: 0, pages: 0 }),
  task = useTask()
watch([group, pageNo], () =>
  sessionStorage.setItem(key, JSON.stringify({ group: group.value, pageNo: pageNo.value })),
)
function load() {
  return task.run(async () => {
    page.value = await request('workerListOrders', {
      query: {
        pageNo: pageNo.value,
        pageSize: 6,
        ...(groups[group.value].statuses ? { statuses: groups[group.value].statuses } : {}),
      },
    })
  })
}
function select(index: number) {
  group.value = index
  pageNo.value = 1
  void load()
}
function turn(delta: number) {
  pageNo.value += delta
  document.getElementById('worker-scroll')?.scrollTo({ top: 0 })
  void load()
}
useRefresh(load)
</script>
<template>
  <OrderGroups :groups="groups" :selected="group" :busy="task.busy.value" @select="select" />
  <p class="muted">进行中优先，待服务按预约先后，历史按结束时间倒序。</p>
  <Feedback :busy="task.busy.value" :error="task.error.value" />
  <button @click="load">刷新订单</button>
  <WorkerOrderCard
    v-for="order in page.list"
    :key="order.id"
    :order="order"
    return-to="/worker/orders"
  />
  <p v-if="!task.busy.value && !page.list.length" class="empty">当前分组暂无订单</p>
  <div v-if="page.pages" class="pagination">
    <button :disabled="task.busy.value || pageNo <= 1" @click="turn(-1)">上一页</button>
    <span>{{ pageNo }} / {{ page.pages }} · {{ page.total }}单</span>
    <button :disabled="task.busy.value || pageNo >= page.pages" @click="turn(1)">下一页</button>
  </div>
</template>
