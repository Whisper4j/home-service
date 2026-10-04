import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export function usePanel() {
  const route = useRoute(),
    router = useRouter()
  let openedHere = false
  const panel = computed(() => (typeof route.query.panel === 'string' ? route.query.panel : ''))
  function open(name: string) {
    openedHere = !panel.value
    return router[openedHere ? 'push' : 'replace']({
      path: route.path,
      query: { ...route.query, panel: name },
    })
  }
  function close() {
    if (openedHere) {
      openedHere = false
      router.back()
      return
    }
    const { panel: _panel, ...query } = route.query
    void router.replace({ path: route.path, query })
  }
  return { panel, open, close }
}
