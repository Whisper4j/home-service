import { onMounted, onUnmounted } from 'vue'
export function useRefresh(load: () => unknown): void {
  const refresh = () => {
    void load()
  }
  onMounted(() => {
    refresh()
    window.addEventListener('data-refresh', refresh)
    window.addEventListener('focus', refresh)
  })
  onUnmounted(() => {
    window.removeEventListener('data-refresh', refresh)
    window.removeEventListener('focus', refresh)
  })
}
