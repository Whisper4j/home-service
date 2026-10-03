<script setup lang="ts">
import { computed, watch } from 'vue'
import { showError } from '../stores/feedback'
const props = defineProps<{
  error?: string
  success?: string
  busy?: boolean
  persistent?: boolean
}>()
watch(
  () => props.error,
  (value) => {
    if (value) showError(value)
  },
  { immediate: true },
)
const critical = computed(
  () =>
    props.persistent ||
    /PRICE_CHANGED|NETWORK_ERROR|SCHEDULE_CONFLICT|报价已变化|结果不确定/.test(props.error || ''),
)
</script>
<template>
  <p v-if="busy" role="status">处理中…</p>
  <p v-if="error && critical" class="feedback error" role="alert">
    {{ error.replace(/（[A-Z_]+）/g, '') }}
  </p>
  <p v-if="success" class="feedback" role="status">{{ success }}</p>
</template>
