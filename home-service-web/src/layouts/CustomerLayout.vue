<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sessions } from '../stores/session'
import { connectNotifications } from '../api/notifications'
import { useMock } from '../api/client'
import '../styles/customer.css'
const route = useRoute(),
  router = useRouter()
const scroll = ref<HTMLElement>(),
  height = ref('100dvh'),
  notice = ref('')
const primary = computed(() =>
  ['/customer/home', '/customer/orders', '/customer/me'].includes(route.path),
)
const title = computed(() => String(route.meta.title || '家政预约'))
function resize() {
  height.value = `${window.visualViewport?.height || window.innerHeight}px`
}
function back() {
  const target = route.query.returnTo
  if (typeof target === 'string' && target.startsWith('/customer/') && !target.includes('/login'))
    void router.push(target)
  else if (window.history.state.back?.startsWith('/customer/')) router.back()
  else void router.push('/customer/home')
}
function refresh() {
  notice.value = ''
  window.dispatchEvent(new Event('data-refresh'))
}
watch(
  () => route.fullPath,
  () => {
    scroll.value?.scrollTo({ top: 0 })
  },
)
watch(
  () => sessions.customer?.accessToken,
  async (token, _old, onCleanup) => {
    let disposed = false,
      disconnect: (() => void) | undefined
    onCleanup(() => {
      disposed = true
      disconnect?.()
      notice.value = ''
    })
    if (!token) return
    disconnect = await connectNotifications(
      token,
      (event) => {
        notice.value =
          event.type === 'OFFER_PRICE_CHANGED'
            ? '预约报价有变化，请查看最新金额后重新确认。'
            : '预约进度有更新，可刷新查看。'
      },
      refresh,
      () => {},
    )
    if (disposed) disconnect()
  },
  { immediate: true },
)
onMounted(() => {
  resize()
  window.visualViewport?.addEventListener('resize', resize)
  window.addEventListener('resize', resize)
})
onUnmounted(() => {
  window.visualViewport?.removeEventListener('resize', resize)
  window.removeEventListener('resize', resize)
})
</script>
<template>
  <div class="customer-shell" :style="{ height }">
    <header class="customer-header">
      <button v-if="!primary" aria-label="返回上一页" @click="back">返回</button>
      <h1>{{ title }}</h1>
      <span class="mode-note">{{ useMock ? '产品原型' : '接口联调' }}</span>
    </header>
    <div v-if="notice" class="customer-notice" role="status">
      <span>{{ notice }}</span>
      <button @click="refresh">刷新</button>
    </div>
    <div ref="scroll" class="customer-scroll" id="customer-scroll">
      <RouterView :key="route.fullPath" />
    </div>
    <div id="customer-actions" class="customer-actions" />
    <nav v-if="primary" class="customer-tabs" aria-label="客户导航">
      <RouterLink to="/customer/home">首页</RouterLink>
      <RouterLink to="/customer/orders">订单</RouterLink>
      <RouterLink to="/customer/me">我的</RouterLink>
    </nav>
  </div>
</template>
