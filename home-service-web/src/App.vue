<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const expired = (event: Event) =>
  router.replace(`/${(event as CustomEvent).detail}/login?expired=1`)
onMounted(() => window.addEventListener('session-expired', expired))
onUnmounted(() => window.removeEventListener('session-expired', expired))
</script>
<template><RouterView /></template>
