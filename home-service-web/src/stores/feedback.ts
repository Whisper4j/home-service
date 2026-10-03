import { ref } from 'vue'
export const toast = ref('')
let timer: ReturnType<typeof setTimeout> | undefined
export function showError(message: string) {
  if (!message) return
  clearTimeout(timer)
  toast.value = message.replace(/（[A-Z_]+）/g, '')
  timer = setTimeout(() => {
    toast.value = ''
  }, 2000)
}
