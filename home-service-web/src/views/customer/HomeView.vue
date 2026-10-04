<script setup lang="ts">
import { computed, ref } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { entryGroups } from '../../utils/entries'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/CustomerFeedback.vue'
const entries = ref<Schema['ClientEntryVO'][]>([]),
  regions = ref<Schema['RegionVO'][]>([])
const { busy, error, run } = useTask()
const groups = computed(() => entryGroups(entries.value))
function load() {
  return run(async () => {
    const [catalog, area] = await Promise.all([
      request('listClientEntries'),
      request('listServiceRegions'),
    ])
    entries.value = catalog
    regions.value = area
  })
}
useRefresh(load)
</script>
<template>
  <div class="home-content">
    <h2>家政预约与调度平台</h2>
    <p v-if="regions.length" class="muted">
      当前服务范围：{{ regions.map((r) => r.cityName).join('、') }}
    </p>
    <Feedback :busy="busy" :error="error" />
    <button v-if="error" @click="load">重新连接服务</button>
    <div class="home-entries">
      <RouterLink
        v-for="group in groups"
        :key="group.groupCode"
        class="button entry-button"
        :to="`/customer/groups/${encodeURIComponent(group.groupCode)}`"
      >
        <strong>{{ group.groupName }}</strong>
        <span>{{ group.groupDescription }}</span>
      </RouterLink>
    </div>
    <p v-if="!busy && !error && !groups.length" class="empty">暂无服务入口</p>
    <p class="muted">先浏览，再预约。支付后安排人员，履约进度随时查看。</p>
  </div>
</template>
