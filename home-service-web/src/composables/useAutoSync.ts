import { onMounted, onUnmounted, ref } from 'vue'
import { errorMessage } from '../api/errors'
import { showError } from '../stores/feedback'

/** Serialize authoritative reads; notifications received in flight request one trailing read. */
export function useAutoSync(read: (active: () => boolean) => Promise<void>) {
  const error = ref(''),
    loading = ref(true)
  let disposed = false,
    running = false,
    pending = false
  let timer: ReturnType<typeof setInterval> | undefined
  async function sync() {
    if (disposed || document.hidden || !navigator.onLine) return
    if (running) {
      pending = true
      return
    }
    running = true
    try {
      do {
        pending = false
        try {
          await read(() => !disposed)
          if (!disposed) error.value = ''
        } catch (reason) {
          if (disposed) return
          const message = errorMessage(reason)
          if (message !== error.value) showError(message)
          error.value = message
        }
      } while (pending && !disposed && !document.hidden)
    } finally {
      running = false
      loading.value = false
    }
  }
  const trigger = () => {
    void sync()
  }
  onMounted(() => {
    trigger()
    for (const name of ['business-notification', 'data-refresh', 'focus', 'online'])
      window.addEventListener(name, trigger)
    document.addEventListener('visibilitychange', trigger)
    timer = setInterval(trigger, 30_000)
  })
  onUnmounted(() => {
    disposed = true
    clearInterval(timer)
    for (const name of ['business-notification', 'data-refresh', 'focus', 'online'])
      window.removeEventListener(name, trigger)
    document.removeEventListener('visibilitychange', trigger)
  })
  return { sync, error, loading }
}
