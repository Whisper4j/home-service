<script setup lang="ts">
defineProps<{ error?: string; success?: string; busy?: boolean }>()
</script>
<template>
  <p v-if="busy" role="status">处理中…</p>
  <p v-if="error" class="feedback error" role="alert">{{ error }}</p>
  <p v-if="success" class="feedback" role="status">{{ success }}</p>
</template>
