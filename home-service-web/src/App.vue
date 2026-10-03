<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
function expired(event: Event) {
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
<template><RouterView /></template>
