import type { Schema } from '../api/types'
import type { Database } from './database'
import { iso } from '../utils/format'
export class MockContext {
  events: Schema['WsEvent'][] = []
  constructor(public db: Database, public readonly clock: () => number = Date.now) {}
  get now(): number { return this.clock() + this.db.clockOffset }
  nextId(): string { return String(++this.db.sequence) }
  event(type: Schema['WsEventType'], order: Schema['OrderVO'], reason?: string): void {
    this.events.push({ eventId: this.nextId(), type, orderId: order.id, priceVersion: order.priceVersion, occurredAt: iso(this.now), payload: { status: order.status, currentPrice: order.currentPrice, ...(reason ? { reason } : {}) } })
  }
  audit(actorId: string, action: string, targetId: string, detail: string): void {
    this.db.audits.unshift({ id: this.nextId(), actorId, action, targetId, detail, createdAt: iso(this.now) })
  }
}
