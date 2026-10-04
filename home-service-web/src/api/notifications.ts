import type { Schema } from './types'

export async function connectNotifications(
  token: string,
  receive: (event: Schema['WsEvent']) => void,
  refresh: () => void,
  status: (text: string) => void,
): Promise<() => void> {
  let stopped = false,
    socket: WebSocket | undefined,
    timer: ReturnType<typeof setTimeout> | undefined,
    attempts = 0
  const seen = new Set<string>()
  const connect = () => {
    if (stopped || document.hidden || !navigator.onLine) return
    if (socket && socket.readyState < WebSocket.CLOSING) return
    const url = new URL('/ws', window.location.href)
    url.protocol = location.protocol === 'https:' ? 'wss:' : 'ws:'
    status('通知连接中')
    socket = new WebSocket(url)
    socket.onopen = () =>
      socket?.send(
        JSON.stringify({ type: 'AUTH', accessToken: token } satisfies Schema['WsAuthFrame']),
      )
    socket.onmessage = (message) => {
      try {
        const event = JSON.parse(message.data) as Schema['WsEvent'] | Schema['WsAuthAck']
        if (event.type === 'AUTHENTICATED') {
          attempts = 0
          status('实时通知已连接')
          refresh()
          return
        }
        if (!event.eventId || seen.has(event.eventId)) return
        seen.add(event.eventId)
        if (seen.size > 500) seen.delete(seen.values().next().value!)
        receive(event)
      } catch {
        status('通知解析失败，将通过HTTP重新同步')
        refresh()
      }
    }
    socket.onclose = (event) => {
      if (stopped) return
      if (event.code === 4401 || event.code === 4403) {
        status('通知认证失效，请重新登录')
        refresh()
        return
      }
      status('通知已断开，正在重连；HTTP同步继续补漏')
      timer = setTimeout(connect, Math.min(30_000, 1000 * 2 ** attempts++))
    }
    socket.onerror = () => status('通知暂不可用，业务操作仍可通过HTTP提交')
  }
  const recover = () => {
    if (document.hidden || !navigator.onLine) return
    clearTimeout(timer)
    connect()
    refresh()
  }
  window.addEventListener('online', recover)
  window.addEventListener('retry-notifications', recover)
  document.addEventListener('visibilitychange', recover)
  connect()
  return () => {
    stopped = true
    clearTimeout(timer)
    socket?.close()
    window.removeEventListener('online', recover)
    window.removeEventListener('retry-notifications', recover)
    document.removeEventListener('visibilitychange', recover)
  }
}
