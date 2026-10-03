<script setup lang="ts">
import { onMounted, ref } from 'vue'
defineProps<{ title: string; busy?: boolean; expanded?: boolean }>()
const emit = defineEmits<{ close: [] }>()
const dialog = ref<HTMLDialogElement>()
onMounted(() => dialog.value?.showModal())
</script>
<template>
  <dialog
    ref="dialog"
    class="customer-dialog flow-dialog"
    :class="{ 'task-dialog': expanded }"
    :aria-label="title"
    @cancel.prevent="!busy && emit('close')"
  >
    <header>
      <h2>{{ title }}</h2>
      <button :disabled="busy" @click="emit('close')">关闭</button>
    </header>
    <div class="dialog-body"><slot /></div>
    <footer v-if="$slots.actions"><slot name="actions" /></footer>
  </dialog>
</template>
