<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
type Row = Schema['DispatchAttemptVO'] | Schema['PaymentVO'] | Schema['AuditVO']
type Resource = 'dispatch-attempts' | 'payments' | 'audits'
const route = useRoute(),
  resource = computed(() => String(route.params.resource) as Resource)
const titles = {
  'dispatch-attempts': '调度尝试与异常',
  payments: '支付与退款流水',
  audits: '关键操作审计',
}
const rows = ref<Row[]>([]),
  total = ref(0),
  pages = ref(0)
const query = reactive({ pageNo: 1, pageSize: 20, orderId: '', keyword: '' }),
  { busy, error, run } = useTask()
function load() {
  return run(async () => {
    const op = (
      {
        'dispatch-attempts': 'listDispatchAttempts',
        payments: 'listPayments',
        audits: 'listAudits',
      } as const
    )[resource.value]
    if (!op) throw Error('无效记录类型')
    const page = await request(op, {
      query: {
        pageNo: query.pageNo,
        pageSize: query.pageSize,
        ...(query.orderId ? { orderId: query.orderId } : {}),
        ...(query.keyword ? { keyword: query.keyword } : {}),
      },
    })
    rows.value = page.list
    total.value = page.total
    pages.value = page.pages
  })
}
function search() {
  query.pageNo = 1
  void load()
}
useRefresh(load)
function changePage(value: number) {
  query.pageNo = value
  void load()
}
function resizePage(value: number) {
  query.pageSize = value
  query.pageNo = 1
  void load()
}
</script>
<template>
  <h1>{{ titles[resource] }}</h1>
  <p v-if="resource === 'dispatch-attempts'">
    自动派单按当天服务分钟数、订单数、人员ID排序；失败尝试不会作为生效分配记录。
  </p>
  <p v-if="resource === 'payments'">
    模拟支付、补差、部分退款、全额退款均保留独立流水。退款由调价、取消或超时业务触发。
  </p>
  <p v-if="resource === 'audits'">目录价格、上下架、账号状态、异常取消等关键操作留痕。</p>
  <form class="filters" @submit.prevent="search">
    <label>
      订单 ID
      <input name="orderId" v-model.trim="query.orderId" pattern="[1-9][0-9]{0,18}" />
    </label>
    <label>
      关键字
      <input name="keyword" v-model.trim="query.keyword" />
    </label>
    <button :disabled="busy">查询</button>
  </form>
  <Feedback :error="error" :busy="busy" />
  <div class="table-scroll">
    <table>
      <thead>
        <tr>
          <th>ID / 时间</th>
          <th>关联资源</th>
          <th>类型 / 结果</th>
          <th>详细信息</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in rows" :key="row.id">
          <td>
            {{ row.id }}
            <br />
            {{ displayTime(row.createdAt) }}
          </td>
          <td>
            <RouterLink v-if="row.orderId" :to="`/admin/orders/${row.orderId}`">
              订单 {{ row.orderId }}
            </RouterLink>
            <template v-if="'targetType' in row">
              操作者 {{ row.actorType === 'SYSTEM' ? '系统任务' : row.actorId }}
              <br />
              目标 {{ label(row.targetType) }} · {{ row.targetId }}
            </template>
          </td>
          <td>
            {{ 'type' in row ? label(row.type) : 'result' in row ? label(row.result) : row.action }}
          </td>
          <td>
            <template v-if="'amount' in row">
              ¥{{ row.amount }}
              <br />
              业务号 {{ row.businessNo }}
            </template>
            <template v-else-if="'reason' in row">
              {{ row.reason }}
              <br />
              候选人员 {{ row.workerId || '无' }} · 当日服务 {{ row.serviceMinutes ?? '—' }} 分钟 ·
              {{ row.orderCount ?? '—' }} 单
            </template>
            <template v-else>{{ row.detail }}</template>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  <p v-if="!busy && !rows.length" class="empty">暂无符合条件的记录。</p>
  <Pagination
    v-bind="query"
    :total="total"
    :pages="pages"
    @change="changePage"
    @resize="resizePage"
  />
</template>
