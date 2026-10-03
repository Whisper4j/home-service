<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { clientEntries } from '../../utils/clientEntries'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/CustomerFeedback.vue'
const route = useRoute(),
  router = useRouter(),
  { busy, error, run } = useTask()
const entries = ref<Schema['ClientEntryVO'][]>([])
const cleaning = computed(() => route.path.includes('/cleaning'))
const group = computed(() => String(route.params.group || ''))
const cards = computed(() =>
  clientEntries
    .filter((e) => e.group === group.value)
    .map((e) => ({ ...e, ...entries.value.find((s) => s.code === e.code) })),
)
function load() {
  return run(async () => {
    entries.value = await request('listClientEntries')
  })
}
function select(code: string) {
  void router.push(`/customer/services/${code}`)
}
useRefresh(load)
</script>
<template>
  <div v-if="cleaning" class="segmented" aria-label="清洁类型">
    <RouterLink to="/customer/cleaning/daily" :aria-current="group === 'daily'">
      日常清洁
    </RouterLink>
    <RouterLink to="/customer/cleaning/deep" :aria-current="group === 'deep'">深度清洁</RouterLink>
  </div>
  <template v-if="!cleaning && !group">
    <h2>需要哪一类维修？</h2>
    <p class="muted">按固定范围预约，不提供上门检测后报价。</p>
    <RouterLink class="button package-card" to="/customer/repair/plumbing">
      <strong>管道与卫浴</strong>
      <span>马桶、水龙头</span>
    </RouterLink>
    <RouterLink class="button package-card" to="/customer/repair/electrical">
      <strong>电气与灯具</strong>
      <span>灯泡、普通灯具、保险丝</span>
    </RouterLink>
    <RouterLink class="button package-card" to="/customer/repair/appliance">
      <strong>家电维护</strong>
      <span>壁挂空调清洗</span>
    </RouterLink>
  </template>
  <template v-else>
    <h2>
      {{ cleaning ? (group === 'daily' ? '按时长选择套餐' : '按面积选择套餐') : '选择具体服务' }}
    </h2>
    <p v-if="group === 'daily'" class="muted">
      按预约时长提供服务，面积仅供参考，实际完成范围与现场整洁程度和清洁内容有关。
    </p>
    <p v-if="group === 'deep'" class="muted">已入住住宅的重点清洁，不包含装修开荒。</p>
    <Feedback :busy="busy" :error="error" />
    <button v-if="error" class="wide-button" @click="load">重新加载服务</button>
    <button
      v-for="entry in cards"
      :key="entry.code"
      class="package-card"
      :disabled="!entry.available || busy"
      @click="select(entry.code)"
    >
      <strong>{{ entry.title }}</strong>
      <span class="muted">{{ entry.hint }}</span>
      <span v-if="entry.sku" class="price">
        {{ entry.sku.durationMinutes }} 分钟 · 标准价 ¥{{ entry.sku.standardPrice }}
      </span>
      <span v-else>{{ busy ? '加载中…' : '暂不可预约' }}</span>
    </button>
    <p v-if="!cards.length" class="empty">没有此服务入口，请返回首页选择。</p>
  </template>
</template>
