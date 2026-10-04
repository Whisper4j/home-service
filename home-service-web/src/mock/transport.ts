import type { OperationId, Schema } from '../api/types'
import { ApiError } from '../api/errors'
import { MockEngine, type MockRequest } from './engine'
import { createDatabase, type Database } from './database'
import { runDueJobs } from './orders'
import { canAssign } from './scheduling'
import { imageStore } from './images'
import { routes } from '../api/generated/routes'

const key = 'home-service.mock.v1'
const channel =
  typeof BroadcastChannel !== 'undefined'
    ? new BroadcastChannel('home-service.events.v1')
    : undefined
const subscribers = new Set<(event: Schema['WsEvent']) => void>()
channel?.addEventListener('message', (event) => subscribers.forEach((fn) => fn(event.data)))
export function readDatabase(): Database {
  try {
    const saved = JSON.parse(localStorage.getItem(key) || 'null')
    if (saved?.version === 1) {
      // 为已有原型数据补足新增快照；仅按明确的初始化 SKU ID 迁移入口，不按名称猜测。
      if (!saved.images) {
        saved.images = []
        const seeds = createDatabase().skus
        for (const sku of saved.skus)
          sku.clientEntryCode = seeds.find((s) => s.id === sku.id)?.clientEntryCode ?? null
        for (const { order } of saved.orders)
          Object.assign(order, {
            contactName: order.address.contactName,
            contactPhone: order.address.contactPhone,
            sceneImages: [],
          })
        // 补齐历史幂等响应中的订单快照，保留原写入结果与幂等键。
        for (const cached of Object.values(saved.idempotency) as {
          response: { data: unknown }
        }[]) {
          const data = cached.response.data as Schema['OrderVO']
          if (data?.service && data.address)
            Object.assign(data, {
              contactName: data.address.contactName,
              contactPhone: data.address.contactPhone,
              sceneImages: [],
            })
        }
      }
      const migrateOrder = (order: Schema['OrderVO']) => {
        order.address.longitude ??= null
        order.address.latitude ??= null
        order.offerPriceRule ||= 'MINIMUM_ANCHORED'
        if (order.offerDeadline && !order.offerPublishedAt) {
          const payment = saved.histories[order.id]?.payments.find(
            (record: Schema['PaymentVO']) => record.type === 'PAYMENT',
          )
          if (payment) order.offerPublishedAt = payment.createdAt
        }
        if (['COMPLETED', 'CANCELLED'].includes(order.status)) order.closedAt ||= order.endTime
      }
      for (const address of saved.addresses) {
        address.longitude ??= null
        address.latitude ??= null
      }
      saved.orders.forEach((stored: { order: Schema['OrderVO'] }) => migrateOrder(stored.order))
      for (const cached of Object.values(saved.idempotency) as {
        response: { data: Schema['OrderVO'] }
      }[]) {
        if (cached.response.data?.service && cached.response.data.address)
          migrateOrder(cached.response.data)
      }
      return saved
    }
  } catch {
    /* 损坏数据由重置恢复 */
  }
  return createDatabase()
}
function publish(events: Schema['WsEvent'][]): void {
  for (const event of events) {
    subscribers.forEach((fn) => fn(event))
    channel?.postMessage(event)
  }
}
async function transaction<T>(action: (engine: MockEngine) => Promise<T> | T): Promise<T> {
  const execute = async () => {
    const engine = new MockEngine(readDatabase())
    try {
      return await action(engine)
    } finally {
      localStorage.setItem(key, JSON.stringify(engine.db))
      publish(engine.events)
    }
  }
  // 同源多标签页串行事务，避免两个演示账号同时抢单时覆盖localStorage。
  if (navigator.locks) return navigator.locks.request('home-service.mock.transaction', execute)
  throw new ApiError(
    'INTERNAL_ERROR',
    '当前浏览器不支持 Web Locks，请使用现代浏览器或 localhost 安全环境',
    500,
  )
}
let failNext = false
export function simulateNetworkFailure(): void {
  failNext = true
}
export async function mockRequest(
  operationId: OperationId,
  request: MockRequest,
): Promise<{ code: 'SUCCESS'; message: string; data: unknown }> {
  if (failNext) {
    failNext = false
    throw new ApiError('NETWORK_ERROR', '演示网络中断，操作尚未发送；请重试', 0)
  }
  return transaction(async (engine) => {
    const before = structuredClone(engine.db)
    try {
      const result = await engine.handle(operationId, request)
      if (routes[operationId].upload)
        await imageStore((store) =>
          store.put(request.file!, (result.data as Schema['SceneImageVO']).id),
        )
      if (operationId === 'deleteSceneImage') await imageStore((store) => store.delete(request.id!))
      if (routes[operationId].binary) {
        const blob = (await imageStore((store) => store.get(request.id!))) as Blob | undefined
        if (!blob)
          throw new ApiError(
            'IMAGE_NOT_AVAILABLE',
            '本地图片内容不可用，请重新上传；订单引用仍保留',
            404,
          )
        result.data = blob
      }
      return result
    } catch (error) {
      if (routes[operationId].upload) engine.db = before
      throw error
    }
  })
}
export function mockNow(): number {
  return Date.now() + readDatabase().clockOffset
}
export async function advanceClock(minutes: number): Promise<void> {
  await transaction((engine) => {
    engine.db.clockOffset += minutes * 60_000
    runDueJobs(engine)
  })
}
export async function resetDemo(): Promise<void> {
  await transaction((engine) => {
    engine.db = createDatabase()
    engine.events = []
  })
}
export async function tickMock(): Promise<void> {
  await transaction((engine) => runDueJobs(engine))
}
export function subscribeMock(
  token: string,
  callback: (event: Schema['WsEvent']) => void,
): () => void {
  const handler = (event: Schema['WsEvent']) => {
    const engine = new MockEngine(readDatabase())
    try {
      const account = engine.accountFor(token),
        order = engine.db.orders.find((o) => o.order.id === event.orderId)?.order
      if (!order) return
      if (
        account.role === 'ADMIN' ||
        order.customerId === account.id ||
        order.workerId === account.id
      )
        callback(event)
      else if (account.role === 'WORKER') {
        const worker = engine.db.workers.find((w) => w.accountId === account.id)!
        if (order.bookingType === 'OFFER' && canAssign(engine, worker, order)) callback(event)
      }
    } catch {
      /* 过期/禁用不推送；下次HTTP调用要求重新登录。 */
    }
  }
  subscribers.add(handler)
  return () => subscribers.delete(handler)
}
