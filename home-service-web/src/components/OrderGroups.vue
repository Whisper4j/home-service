<script setup lang="ts">
defineProps<{ groups: { name: string }[]; selected: number; busy?: boolean }>()
const emit = defineEmits<{ select: [index: number] }>()
function choose(index: number, event: MouseEvent) {
  ;(event.currentTarget as HTMLElement).scrollIntoView({ block: 'nearest', inline: 'nearest' })
  emit('select', index)
}
</script>
<template>
  <nav class="order-groups" aria-label="订单分组">
    <button
      v-for="(group, index) in groups"
      :key="group.name"
      :disabled="busy"
      :aria-pressed="selected === index"
      @click="choose(index, $event)"
    >
      {{ group.name }}
    </button>
  </nav>
</template>
