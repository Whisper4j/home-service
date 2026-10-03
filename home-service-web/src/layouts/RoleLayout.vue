<script setup lang="ts">
import { defineAsyncComponent, onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { RolePath, Schema } from '../api/types'
import { clearSession, sessions } from '../stores/session'
import { useMock } from '../api/client'
import { connectNotifications } from '../api/notifications'
import { displayTime } from '../utils/format'
import { label, roleLabels } from '../utils/labels'
const DemoPanel = defineAsyncComponent(() => import('../components/DemoPanel.vue'))
const props = defineProps<{ role: RolePath }>()
const router = useRouter()
const connection = ref('通知连接中'),
  events = ref<Schema['WsEvent'][]>([])
const links = {
  customer: [
    ['catalog', '服务目录'],
    ['addresses', '我的地址'],
    ['orders', '我的预约'],
  ],
  worker: [
    ['orders', '我的服务订单'],
    ['offers', '优惠抢单池'],
    ['schedule', '排班与请假'],
  ],
  admin: [
    ['orders', '订单管理'],
    ['accounts', '账号管理'],
    ['workers', '服务人员'],
    ['catalog/categories', '服务分类'],
    ['catalog/service-items', '服务项目'],
    ['catalog/skus', '服务规格与价格'],
    ['catalog/skills', '技能管理'],
    ['records/dispatch-attempts', '调度与异常'],
    ['records/payments', '支付与退款'],
    ['records/audits', '操作审计'],
    ['settings', '平台配置'],
  ],
}
let disconnect: (() => void) | undefined,
  disposed = false
function refresh() {
  window.dispatchEvent(new Event('data-refresh'))
}
onMounted(async () => {
  const session = sessions[props.role]
  if (!session) return
  disconnect = await connectNotifications(
    session.accessToken,
    (event) => {
      events.value = [event, ...events.value.filter((e) => e.eventId !== event.eventId)].slice(0, 8)
      // 保留正在确认的表单快照，列表显示通知后由用户刷新，避免静默替换报价。
    },
    refresh,
    (text) => {
      connection.value = text
    },
  )
  if (disposed) disconnect()
})
onUnmounted(() => {
  disposed = true
  disconnect?.()
})
function logout() {
  clearSession(props.role)
  void router.push(`/${props.role}/login`)
}
</script>
<template>
  <div class="shell">
    <header>
      <strong>家政预约与调度平台 · {{ roleLabels[role] }}端</strong>
      <span>{{ sessions[role]?.account.displayName }}</span>
      <button @click="logout">退出登录</button>
    </header>
    <aside>
      <nav aria-label="业务导航">
        <RouterLink v-for="[path, name] in links[role]" :key="path" :to="`/${role}/${path}`">
          {{ name }}
        </RouterLink>
      </nav>
      <hr />
      <p>切换端入口</p>
      <nav aria-label="三端入口">
        <RouterLink v-for="(name, key) in roleLabels" :key="key" :to="`/${key}/login`">
          {{ name }}端
        </RouterLink>
      </nav>
    </aside>
    <main>
      <DemoPanel v-if="useMock" />
      <details class="notifications">
        <summary>{{ connection }} · 最近通知 {{ events.length }} 条</summary>
        <p>收到通知后刷新获取最新状态；提交仍会核验报价和版本。</p>
        <ul>
          <li v-for="event in events" :key="event.eventId">
            {{ displayTime(event.occurredAt) }} · {{ label(event.type) }} · 订单
            {{ event.orderId }} · 版本 {{ event.priceVersion }}
          </li>
        </ul>
        <button @click="refresh">刷新当前数据</button>
      </details>
      <RouterView :key="$route.fullPath" />
    </main>
  </div>
</template>
