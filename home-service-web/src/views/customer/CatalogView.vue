<script setup lang="ts">
import { reactive, ref } from 'vue'
import { request } from '../../api/client'
import { allPages } from '../../api/pagination'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
const categories = ref<Schema['CategoryVO'][]>([]),
  items = ref<Schema['ServiceItemVO'][]>([])
const page = ref<Schema['SkuPageDTO']>({ list: [], total: 0, pages: 0 })
const query = reactive({ pageNo: 1, pageSize: 20, keyword: '', categoryId: '', itemId: '' })
const { busy, error, run } = useTask()
async function load() {
  await run(async () => {
    const [categoryPage, itemPage, skuPage] = await Promise.all([
      allPages((n) => request('listCustomerCategory', { query: { pageNo: n, pageSize: 100 } })),
      allPages((n) =>
        request('listCustomerServiceItem', {
          query: {
            pageNo: n,
            pageSize: 100,
            ...(query.categoryId ? { categoryId: query.categoryId } : {}),
          },
        }),
      ),
      request('listCustomerSku', {
        query: {
          pageNo: query.pageNo,
          pageSize: query.pageSize,
          ...(query.keyword ? { keyword: query.keyword } : {}),
          ...(query.categoryId ? { categoryId: query.categoryId } : {}),
          ...(query.itemId ? { itemId: query.itemId } : {}),
        },
      }),
    ])
    categories.value = categoryPage
    items.value = itemPage
    page.value = skuPage
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
function changeCategory() {
  query.itemId = ''
  search()
}
</script>
<template>
  <h1>服务目录</h1>
  <p>广州全市服务 · 单次预约一个规格、数量为1 · 服务范围与配件要求请查看规格说明。</p>
  <form class="filters" @submit.prevent="search">
    <label>
      服务名称
      <input v-model.trim="query.keyword" maxlength="100" />
    </label>
    <label>
      分类
      <select v-model="query.categoryId" @change="changeCategory">
        <option value="">全部分类</option>
        <option v-for="category in categories" :key="category.id" :value="category.id">
          {{ category.name }}
        </option>
      </select>
    </label>
    <label>
      项目
      <select v-model="query.itemId">
        <option value="">全部项目</option>
        <option v-for="item in items" :key="item.id" :value="item.id">{{ item.name }}</option>
      </select>
    </label>
    <button :disabled="busy">筛选</button>
  </form>
  <Feedback :error="error" :busy="busy" />
  <p v-if="!busy && !error && !page.list.length" class="empty">暂无符合条件的服务。</p>
  <div class="grid">
    <article v-for="sku in page.list" :key="sku.id" class="panel">
      <p class="muted">{{ sku.categoryName }} / {{ sku.itemName }}</p>
      <h2>{{ sku.name }}</h2>
      <p>标准价 ¥{{ sku.standardPrice }} / {{ sku.unit }} · {{ sku.durationMinutes }} 分钟</p>
      <p>
        {{ sku.supportsOffer ? `支持优惠预约，最低 ¥${sku.minimumOfferPrice}` : '仅支持标准预约' }}
      </p>
      <p>{{ sku.description }}</p>
      <details>
        <summary>服务包含与排除范围</summary>
        <p>包含：{{ sku.included }}</p>
        <p>不包含：{{ sku.excluded }}</p>
        <p>客户自备配件：{{ sku.customerSuppliesParts ? '需要' : '不需要' }}</p>
      </details>
      <div class="actions">
        <RouterLink class="button" :to="`/customer/booking/${sku.id}`">预约此服务</RouterLink>
      </div>
    </article>
  </div>
  <Pagination
    v-bind="query"
    :total="page.total"
    :pages="page.pages"
    :busy="busy"
    @change="changePage"
    @resize="resizePage"
  />
</template>
