<script setup lang="ts">
import { computed } from 'vue'
const props = defineProps<{ busy?: boolean; error?: string; success?: string }>()
const friendlyError = computed(() =>
  props.error?.replace(/（[A-Z_]+）/g, '').replace(/，版本 \d+/g, ''),
)
</script>
<template>
  <p v-if="busy" role="status" class="muted">正在处理，请稍候…</p>
  <p v-if="friendlyError" role="alert" class="feedback error">{{ friendlyError }}</p>
  <p v-if="success" role="status" class="feedback">{{ success }}</p>
</template>
