<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sessions } from '../stores/session'
import { connectNotifications } from '../api/notifications'
import { useMock } from '../api/client'
import { parentRoute, scrollPositions } from '../utils/navigation'
import '../styles/customer.css'
const props = defineProps<{ role: 'customer' | 'worker' }>()
const route = useRoute(),
  router = useRouter(),
  scroll = ref<HTMLElement>(),
  height = ref('100dvh'),
  notice = ref(''),
  connection = ref('')
const primary = computed(() =>
  ['home', 'orders', 'me'].some((p) => route.path === `/${props.role}/${p}`),
)
const title = computed(() =>
  String(route.meta.title || (props.role === 'worker' ? '服务人员' : '家政预约')),
)
let restoring = false,
  observer: MutationObserver | undefined,
  restoreFrame = 0
function resize() {
  height.value = `${window.visualViewport?.height || window.innerHeight}px`
  document.documentElement.style.setProperty('--viewport-height', height.value)
  document.documentElement.style.setProperty(
    '--viewport-offset',
    `${window.visualViewport?.offsetTop || 0}px`,
  )
}
function back() {
  void router.replace(parentRoute(route))
}
function refresh() {
  notice.value = ''
  window.dispatchEvent(new Event('data-refresh'))
}
function savePosition() {
  if (!restoring && scroll.value) scrollPositions.set(route.fullPath, scroll.value.scrollTop)
}
async function restore() {
  observer?.disconnect()
  cancelAnimationFrame(restoreFrame)
  restoring = true
  await nextTick()
  const target = scrollPositions.get(route.fullPath) || 0
  const apply = () => {
    cancelAnimationFrame(restoreFrame)
    // 等待本轮数据与加载提示一起渲染，避免提示移除后浏览器滚动锚点再次偏移。
    restoreFrame = requestAnimationFrame(() => {
      if (!scroll.value) return
      scroll.value.scrollTop = target
      if (!target || scroll.value.scrollHeight - scroll.value.clientHeight >= target) {
        restoring = false
        observer?.disconnect()
      }
    })
  }
  if (scroll.value) {
    observer = new MutationObserver(apply)
    observer.observe(scroll.value, { childList: true, subtree: true })
    apply()
  }
}
watch(() => route.fullPath, restore, { flush: 'post' })
watch(
  () => sessions[props.role]?.accessToken,
  async (token, _old, cleanup) => {
    let disposed = false,
      disconnect: (() => void) | undefined
    cleanup(() => {
      disposed = true
      disconnect?.()
      notice.value = ''
      connection.value = ''
    })
    if (!token) return
    disconnect = await connectNotifications(
      token,
      (event) => {
        window.dispatchEvent(new CustomEvent('business-notification', { detail: event }))
        if (props.role === 'customer')
          notice.value =
            event.type === 'OFFER_PRICE_CHANGED'
              ? '预约报价有变化，请查看最新金额后重新确认。'
              : '预约进度有更新，可刷新查看。'
        else if (route.path !== '/worker/home') notice.value = '任务或接单资格有更新，请刷新查看。'
      },
      refresh,
      (text) => {
        connection.value = text
      },
    )
    if (disposed) disconnect()
  },
  { immediate: true },
)
function userScroll() {
  cancelAnimationFrame(restoreFrame)
  restoring = false
  observer?.disconnect()
}
onMounted(() => {
  resize()
  void restore()
  window.visualViewport?.addEventListener('resize', resize)
  window.visualViewport?.addEventListener('scroll', resize)
  window.addEventListener('resize', resize)
})
onUnmounted(() => {
  cancelAnimationFrame(restoreFrame)
  observer?.disconnect()
  window.visualViewport?.removeEventListener('resize', resize)
  window.visualViewport?.removeEventListener('scroll', resize)
  window.removeEventListener('resize', resize)
})
</script>
<template>
  <div class="customer-shell" :class="{ 'worker-shell': role === 'worker' }" :style="{ height }">
    <header class="customer-header">
      <button v-if="!primary" aria-label="返回上一页" @click="back">返回</button>
      <h1>{{ title }}</h1>
      <span class="mode-note">{{ useMock ? '产品原型' : '接口联调' }}</span>
    </header>
    <div v-if="notice" class="customer-notice" role="status">
      <span>{{ notice }}</span>
      <button @click="refresh">刷新</button>
    </div>
    <div
      v-if="/断开|不可用|失效|失败|连接中/.test(connection)"
      class="customer-notice"
      role="status"
    >
      <span>{{ connection }}</span>
      <button @click="refresh">重新查询</button>
    </div>
    <div
      ref="scroll"
      :id="`${role}-scroll`"
      class="customer-scroll"
      @scroll="savePosition"
      @wheel.passive="userScroll"
      @touchstart.passive="userScroll"
    >
      <RouterView :key="route.fullPath" />
    </div>
    <div :id="`${role}-actions`" class="customer-actions" />
    <nav
      v-if="primary"
      class="customer-tabs"
      :aria-label="role === 'customer' ? '客户导航' : '人员导航'"
    >
      <RouterLink replace :to="`/${role}/home`">首页</RouterLink>
      <RouterLink replace :to="`/${role}/orders`">订单</RouterLink>
      <RouterLink replace :to="`/${role}/me`">我的</RouterLink>
    </nav>
  </div>
</template>
