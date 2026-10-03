<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import ErrorToast from './components/ErrorToast.vue'
import { showError } from './stores/feedback'
const router = useRouter()
function expired(event: Event) {
  showError('登录已失效，请重新登录后继续。')
  const role = (event as CustomEvent).detail
  const current = router.currentRoute.value
  if (current.path.includes('/login')) return
  void router.replace({
    path: `/${role}/login`,
    query: { expired: '1', redirect: current.fullPath },
  })
}
onMounted(() => window.addEventListener('session-expired', expired))
onUnmounted(() => window.removeEventListener('session-expired', expired))
</script>
<template>
  <RouterView />
  <ErrorToast />
</template>
