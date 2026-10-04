import { ref } from 'vue'
export const toast = ref('')
export const toastKind = ref<'error' | 'success'>('error')
let timer: ReturnType<typeof setTimeout> | undefined
export function showError(message: string) {
  toastKind.value = 'error'
  if (!message) return
  clearTimeout(timer)
  toast.value = message.replace(/（[A-Z_]+）/g, '')
  timer = setTimeout(() => {
    toast.value = ''
  }, 2000)
}
export function showSuccess(message: string) {
  showError(message)
  toastKind.value = 'success'
}
