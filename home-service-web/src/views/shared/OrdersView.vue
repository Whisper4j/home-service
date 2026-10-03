<script setup lang="ts">
import { reactive, ref } from 'vue'
import { request } from '../../api/client'
import type { RolePath, Schema } from '../../api/types'
import { displayTime } from '../../utils/format'
import { label, orderLabels } from '../../utils/labels'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
const props = defineProps<{ role: RolePath }>()
const page = ref<Schema['OrderPageDTO']>({ list: [], total: 0, pages: 0 })
const query = reactive({
  pageNo: 1,
  pageSize: 20,
  keyword: '',
  status: '' as Schema['OrderStatus'] | '',
  bookingType: '' as Schema['BookingType'] | '',
  from: '',
  to: '',
})
const { busy, error, run } = useTask()
async function load() {
  await run(async () => {
    page.value = await request(
      (
        {
          customer: 'customerListOrders',
          worker: 'workerListOrders',
          admin: 'adminListOrders',
        } as const
      )[props.role],
      {
        query: {
          pageNo: query.pageNo,
          pageSize: query.pageSize,
          ...(query.keyword ? { keyword: query.keyword } : {}),
          ...(query.status ? { status: query.status } : {}),
          ...(query.bookingType ? { bookingType: query.bookingType } : {}),
          ...(query.from ? { from: query.from } : {}),
          ...(query.to ? { to: query.to } : {}),
        },
      },
    )
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
  <h1>{{ role === 'admin' ? '订单管理' : role === 'worker' ? '我的服务订单' : '我的预约' }}</h1>
  <p v-if="role === 'worker'">标准预约由系统直接分配，无需接受或拒绝；优惠预约请前往抢单池。</p>
  <p v-if="role === 'admin'">
    查看自动调度与履约，异常订单可进入详情取消；平台不提供手工派单或改派。
  </p>
  <form class="filters" @submit.prevent="search">
    <label>
      订单号 / 服务
      <input v-model.trim="query.keyword" maxlength="100" />
    </label>
    <label>
      履约状态
      <select v-model="query.status">
        <option value="">全部</option>
        <option v-for="(text, status) in orderLabels" :key="status" :value="status">
          {{ text }}
        </option>
      </select>
    </label>
    <label>
      预约方式
      <select v-model="query.bookingType">
        <option value="">全部</option>
        <option value="STANDARD">标准预约</option>
        <option value="OFFER">优惠预约</option>
      </select>
    </label>
    <label>
      开始日期
      <input v-model="query.from" type="date" />
    </label>
    <label>
      结束日期
      <input v-model="query.to" type="date" />
    </label>
    <button :disabled="busy">筛选 / 刷新</button>
  </form>
  <Feedback :error="error" :busy="busy" />
  <div class="table-scroll">
    <table>
      <thead>
        <tr>
          <th>订单 / 服务</th>
          <th>预约时间</th>
          <th>方式 / 金额</th>
          <th>履约</th>
          <th>支付</th>
          <th>调度 / 人员</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in page.list" :key="order.id">
          <td>
            {{ order.id }}
            <br />
            {{ order.service.skuName }}
          </td>
          <td class="nowrap">{{ displayTime(order.startTime) }}</td>
          <td>
            {{ label(order.bookingType) }}
            <br />
            ¥{{ order.dealPrice || order.currentPrice }}
          </td>
          <td>{{ label(order.status) }}</td>
          <td>{{ label(order.paymentStatus) }}</td>
          <td>
            {{ label(order.dispatchStatus) }}
            <br />
            {{ order.workerName || '尚未分配' }}
          </td>
          <td><RouterLink :to="`/${role}/orders/${order.id}`">查看详情</RouterLink></td>
        </tr>
      </tbody>
    </table>
  </div>
  <p v-if="!busy && !error && !page.list.length" class="empty">暂无符合条件的订单。</p>
  <Pagination
    v-bind="query"
    :total="page.total"
    :pages="page.pages"
    :busy="busy"
    @change="changePage"
    @resize="resizePage"
  />
</template>
