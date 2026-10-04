<script setup lang="ts">
import { ref } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { sessions } from '../../stores/session'
import { useAutoSync } from '../../composables/useAutoSync'
import { usePanel } from '../../composables/usePanel'
import WorkerCalendar from '../../components/WorkerCalendar.vue'
import ProfilePanel from '../../components/ProfilePanel.vue'
import ModalPanel from '../../components/ModalPanel.vue'
import LogoutPanel from '../../components/LogoutPanel.vue'
import WorkerRules from '../../components/WorkerRules.vue'
const { panel, open, close } = usePanel()
const profile = ref<Schema['WorkerProfileVO']>()
const stats = ref<Schema['WorkerStatisticsVO']>()
const { error, sync } = useAutoSync(async (active) => {
  const [data, person] = await Promise.all([
    request('getWorkerStatistics'),
    request('getWorkerProfile'),
  ])
  if (active()) {
    stats.value = data
    profile.value = person
  }
})
</script>
<template>
  <section class="identity">
    <h2>
      <button class="text-action identity-name" @click="open('profile')">
        {{ sessions.worker?.account.displayName }}
      </button>
    </h2>
    <p class="muted">服务人员 · {{ profile?.cityName }} · 已登录</p>
  </section>
  <section aria-label="服务数据" class="statistics-summary">
    <p v-if="stats">
      今日待服务 {{ stats.todayPendingCount }} 单 · 本月已完成 {{ stats.monthCompletedCount }} 单 ·
      累计已完成 {{ stats.totalCompletedCount }} 单
    </p>
    <p v-else>正在查询服务数据…</p>
    <small class="muted">北京时间；今日按预约日期，完成按确认时间，不含待确认。</small>
    <p v-if="error">
      {{ error }}
      <button @click="sync">重试服务数据</button>
    </p>
  </section>
  <WorkerCalendar />
  <div class="compact-links">
    <button class="text-action" @click="open('rules')">服务规则 →</button>
    <button class="text-action" @click="open('logout')">退出登录</button>
  </div>
  <ProfilePanel v-if="panel === 'profile'" role="worker" @close="close" />
  <ModalPanel v-if="panel === 'rules'" title="服务规则" @close="close"><WorkerRules /></ModalPanel>
  <LogoutPanel v-if="panel === 'logout'" role="worker" @close="close" />
</template>
