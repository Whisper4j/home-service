<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { displayTime } from '../../utils/format'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
const page = ref<Schema['OfferPageDTO']>({ list: [], total: 0, pages: 0 }),
  selection = ref<Schema['OfferVO']>()
const query = reactive({ pageNo: 1, pageSize: 20, keyword: '', from: '', to: '' })
const router = useRouter(),
  { busy, error, success, run } = useTask()
async function load() {
  await run(async () => {
    page.value = await request('listEligibleOffers', {
      query: {
        pageNo: query.pageNo,
        pageSize: query.pageSize,
        ...(query.keyword ? { keyword: query.keyword } : {}),
        ...(query.from ? { from: query.from } : {}),
        ...(query.to ? { to: query.to } : {}),
      },
    })
  })
}
function search() {
  query.pageNo = 1
  void load()
}
function select(offer: Schema['OfferVO']) {
  selection.value = { ...offer }
  success.value = ''
  error.value = ''
}
function claim() {
  if (!selection.value) return
  const selected = selection.value
  void run(
    async (key) => {
      const order = await request('claimOffer', {
        id: selected.id,
        body: { expectedPrice: selected.currentPrice, priceVersion: selected.priceVersion },
        idempotencyKey: key,
      })
      selection.value = undefined
      await router.push(`/worker/orders/${order.id}`)
    },
    '',
    JSON.stringify(selected),
  )
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
  <h1>优惠抢单池</h1>
  <p>
    仅展示技能、广州服务区域、排班、请假以及服务和缓冲槽均符合的订单。接单前隐藏门牌和联系方式。
  </p>
  <form class="filters" @submit.prevent="search">
    <label>
      服务名称
      <input v-model.trim="query.keyword" />
    </label>
    <label>
      开始日期
      <input v-model="query.from" type="date" />
    </label>
    <label>
      结束日期
      <input v-model="query.to" type="date" />
    </label>
    <button :disabled="busy">筛选 / 刷新报价</button>
  </form>
  <Feedback :error="error" :success="success" :busy="busy" />
  <section v-if="selection" class="panel">
    <h2>确认抢单</h2>
    <p>
      订单 {{ selection.id }} · {{ selection.skuName }} · ¥{{ selection.currentPrice }} · 价格版本
      {{ selection.priceVersion }}
    </p>
    <p>将按以上价格与版本提交；如有变更或订单被抢会明确失败，不自动按新价格成交。</p>
    <div class="actions">
      <button :disabled="busy" @click="claim">按此价格确认抢单</button>
      <button @click="selection = undefined">关闭确认</button>
    </div>
  </section>
  <div class="grid">
    <article v-for="offer in page.list" :key="offer.id" class="panel">
      <h2>{{ offer.skuName }}</h2>
      <p>订单 {{ offer.id }} · 广州市{{ offer.districtName }}</p>
      <p>
        <strong>¥{{ offer.currentPrice }}</strong>
        · 版本 {{ offer.priceVersion }}
      </p>
      <p>
        开始：{{ displayTime(offer.startTime) }}
        <br />
        服务结束：{{ displayTime(offer.endTime) }}
        <br />
        缓冲结束：{{ displayTime(offer.bufferEndTime) }}
        <br />
        接单截止：{{ displayTime(offer.offerDeadline) }}
      </p>
      <details>
        <summary>服务范围</summary>
        <p>{{ offer.description }}</p>
        <p>包含：{{ offer.included }}</p>
        <p>排除：{{ offer.excluded }}</p>
      </details>
      <button :disabled="busy" @click="select(offer)">查看并确认抢单</button>
    </article>
  </div>
  <p v-if="!busy && !page.list.length" class="empty">
    暂无符合条件的优惠订单。请检查排班、技能、派单开关和已有时间占用。
  </p>
  <Pagination
    v-bind="query"
    :total="page.total"
    :pages="page.pages"
    :busy="busy"
    @change="changePage"
    @resize="resizePage"
  />
</template>
