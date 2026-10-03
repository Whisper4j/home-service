import type { Schema } from './types'
import { useMock } from './client'

export async function connectNotifications(token: string, receive: (event: Schema['WsEvent']) => void, refresh: () => void, status: (text: string) => void): Promise<() => void> {
  if (useMock) {
    const { subscribeMock } = await import('../mock/transport')
    status('演示通知已连接'); refresh()
    return subscribeMock(token, receive)
  }
  let stopped = false, socket: WebSocket | undefined, timer: ReturnType<typeof setTimeout> | undefined, attempts = 0
  const seen = new Set<string>()
  const connect = () => {
    const url = new URL('/ws', window.location.href); url.protocol = location.protocol === 'https:' ? 'wss:' : 'ws:'
    status('通知连接中')
    socket = new WebSocket(url)
    socket.onopen = () => socket?.send(JSON.stringify({ type: 'AUTH', accessToken: token } satisfies Schema['WsAuthFrame']))
    socket.onmessage = message => {
      try {
        const event = JSON.parse(message.data) as Schema['WsEvent'] | Schema['WsAuthAck']
        if (event.type === 'AUTHENTICATED') { attempts = 0; status('实时通知已连接'); refresh(); return }
        if (!event.eventId || seen.has(event.eventId)) return
        seen.add(event.eventId); if (seen.size > 500) seen.delete(seen.values().next().value!)
        receive(event)
      } catch { status('通知解析失败，请刷新列表') }
    }
    socket.onclose = event => {
      if (stopped) return
      if (event.code === 4401 || event.code === 4403) { status('通知认证失效，请重新登录'); refresh(); return }
      status('通知已断开，正在重连；可手动刷新')
      timer = setTimeout(connect, Math.min(30_000, 1000 * 2 ** attempts++))
    }
    socket.onerror = () => status('通知暂不可用，业务操作仍可通过HTTP提交')
  }
  connect()
  return () => { stopped = true; clearTimeout(timer); socket?.close() }
}
