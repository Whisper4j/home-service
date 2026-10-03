<script setup lang="ts">
import Feedback from './Feedback.vue'
defineProps<{ busy?: boolean; error?: string; success?: string; persistent?: boolean }>()
</script>
<template>
  <Feedback v-bind="$props" />
</template>
