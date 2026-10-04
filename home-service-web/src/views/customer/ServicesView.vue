<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { sortedEntries, entryGroups } from '../../utils/entries'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/CustomerFeedback.vue'
const route = useRoute(),
  router = useRouter(),
  { busy, error, run } = useTask()
const entries = ref<Schema['ClientEntryVO'][]>([])
const cards = computed(() =>
  sortedEntries(entries.value).filter((e) => e.groupCode === route.params.group),
)
const groups = computed(() => entryGroups(entries.value))
function load() {
  return run(async () => {
    entries.value = await request('listClientEntries')
  })
}
function select(code: string) {
  void router.push({
    path: `/customer/services/${encodeURIComponent(code)}`,
    query: { group: String(route.params.group) },
  })
}
useRefresh(load)
</script>
<template>
  <nav class="segmented" aria-label="服务分组">
    <RouterLink
      v-for="group in groups"
      :key="group.groupCode"
      replace
      :to="`/customer/groups/${encodeURIComponent(group.groupCode)}`"
      :aria-current="route.params.group === group.groupCode"
    >
      {{ group.groupName }}
    </RouterLink>
  </nav>
  <h2>{{ cards[0]?.groupName || '选择服务' }}</h2>
  <p class="muted">{{ cards[0]?.groupDescription }}</p>
  <Feedback :busy="busy" :error="error" />
  <button v-if="error" @click="load">重新加载服务</button>
  <button
    v-for="entry in cards"
    :key="entry.code"
    class="package-card"
    :disabled="!entry.available || !entry.sku || busy"
    @click="select(entry.code)"
  >
    <strong>{{ entry.name }}</strong>
    <span>{{ entry.description }}</span>
    <span v-if="entry.sku">
      {{ entry.sku.durationMinutes }} 分钟 · 标准价 ¥{{ entry.sku.standardPrice }}
    </span>
    <span v-if="!entry.available">{{ entry.unavailableReason }}</span>
  </button>
  <p v-if="!busy && !error && !cards.length" class="empty">暂无服务，请返回首页选择。</p>
</template>
