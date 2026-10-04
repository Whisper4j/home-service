<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { onBeforeRouteLeave, onBeforeRouteUpdate } from 'vue-router'
const props = defineProps<{ title: string; busy?: boolean; expanded?: boolean; dirty?: boolean }>()
const emit = defineEmits<{ close: []; discard: [] }>()
const dialog = ref<HTMLDialogElement>()
const discarding = ref(false)
let approved = false
let scroll: HTMLElement | null = null,
  position = 0,
  overflow = ''
function close() {
  if (props.busy) return
  if (props.dirty) discarding.value = true
  else {
    approved = true
    emit('close')
  }
}
function discard() {
  approved = true
  emit('discard')
  emit('close')
}
function allowNavigation(to: { path: string }, from: { path: string }) {
  if (to.path.includes('/login') || approved || !props.dirty) return true
  if (to.path.includes('/orders/') && from.path.endsWith('/me')) return true
  const discardChanges = window.confirm('有未保存的修改，是否放弃修改并离开？')
  if (discardChanges) emit('discard')
  return discardChanges
}
onBeforeRouteLeave(allowNavigation)
onBeforeRouteUpdate(allowNavigation)
onMounted(() => {
  scroll = document.querySelector('.customer-scroll')
  if (scroll) {
    position = scroll.scrollTop
    overflow = scroll.style.overflow
    scroll.style.overflow = 'hidden'
  }
  dialog.value?.showModal()
})
onUnmounted(() => {
  if (scroll) {
    scroll.style.overflow = overflow
    scroll.scrollTop = position
  }
})
</script>
<template>
  <dialog
    ref="dialog"
    class="customer-dialog flow-dialog"
    :class="{ 'task-dialog': expanded }"
    :aria-label="title"
    @cancel.prevent="close"
  >
    <header>
      <h2>{{ title }}</h2>
      <button :disabled="busy" @click="close">关闭</button>
    </header>
    <div v-if="discarding" class="dialog-body" role="alert">
      <p>有未保存的修改，确定放弃吗？</p>
      <button @click="discarding = false">继续编辑</button>
      <button @click="discard">放弃修改</button>
    </div>
    <div v-else class="dialog-body"><slot /></div>
    <footer v-if="!discarding && $slots.actions"><slot name="actions" /></footer>
  </dialog>
</template>
