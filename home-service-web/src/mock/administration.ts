import type { Schema } from '../api/types'
import { fail } from '../api/errors'
import { cents } from '../utils/format'
import type { MockContext } from './context'

export type CatalogResource = 'categories' | 'service-items' | 'skus' | 'skills'
export type CatalogEntry = Schema['CategoryVO'] | Schema['ServiceItemVO'] | Schema['SkuVO'] | Schema['SkillVO']
export function catalogRows(context: MockContext, resource: CatalogResource): CatalogEntry[] {
  return resource === 'service-items' ? context.db.items : context.db[resource]
}
export function writeCatalog(context: MockContext, actor: string, resource: CatalogResource, id: string | undefined, body: unknown): CatalogEntry {
  const rows = catalogRows(context, resource)
  if (id && !rows.some(row => row.id === id)) fail('NOT_FOUND', '目录数据不存在', 404)
  const data = structuredClone(body) as CatalogEntry
  data.id = id || context.nextId()
  if (resource === 'service-items') {
    const item = data as Schema['ServiceItemVO']
    if (!context.db.categories.some(c => c.id === item.categoryId)) fail('NOT_FOUND', '分类不存在', 404)
    if (item.serviceKind !== 'CLEANING' && context.db.skus.some(s => s.itemId === item.id && s.supportsOffer)) fail('OFFER_NOT_SUPPORTED', '请先关闭该项目下规格的优惠能力，再修改业务性质', 422)
  }
  if (resource === 'skus') {
    const sku = data as Schema['SkuVO']
    const item = context.db.items.find(i => i.id === sku.itemId)
    if (!item) fail('NOT_FOUND', '服务项目不存在', 404)
    if (sku.skillIds.some(s => !context.db.skills.some(k => k.id === s))) fail('NOT_FOUND', '技能不存在', 404)
    const category = context.db.categories.find(c => c.id === item.categoryId)!
    if (cents(sku.standardPrice) <= 0 || cents(sku.minimumOfferPrice) <= 0 || (sku.supportsOffer ? cents(sku.minimumOfferPrice) >= cents(sku.standardPrice) : sku.minimumOfferPrice !== sku.standardPrice)) fail('PRICE_OUT_OF_RANGE', '请检查标准价与最低报价；不支持优惠时两者须相等', 422)
    if (item.serviceKind !== 'CLEANING' && sku.supportsOffer) fail('OFFER_NOT_SUPPORTED', '当前只有清洁项目可支持优惠', 422)
    sku.categoryId = category.id; sku.categoryName = category.name; sku.itemName = item.name
  }
  if (rows.some(r => r.id !== data.id && r.name === data.name)) fail('RESOURCE_IN_USE', '同类目录名称不能重复')
  const index = rows.findIndex(row => row.id === data.id)
  if (index >= 0) rows[index] = data
  else rows.unshift(data)
  // 当前目录名称跟随编辑，订单内快照不变。
  for (const sku of context.db.skus) {
    const item = context.db.items.find(i => i.id === sku.itemId)!
    const category = context.db.categories.find(c => c.id === item.categoryId)!
    sku.itemName = item.name; sku.categoryId = category.id; sku.categoryName = category.name
  }
  context.audit(actor, id ? 'CATALOG_UPDATED' : 'CATALOG_CREATED', data.id, `${resource}: ${data.name}`)
  return data
}
export function deleteCatalog(context: MockContext, actor: string, resource: CatalogResource, id: string): Schema['MutationVO'] {
  const rows = catalogRows(context, resource), row = rows.find(r => r.id === id)
  if (!row) fail('NOT_FOUND', '数据不存在', 404)
  if ('status' in row && row.status !== 'OFF_SHELF') fail('RESOURCE_IN_USE', '请先下架后再删除')
  const used = resource === 'categories' ? context.db.items.some(i => i.categoryId === id) : resource === 'service-items' ? context.db.skus.some(s => s.itemId === id) : resource === 'skills' ? context.db.workers.some(w => w.skillIds.includes(id)) || context.db.skus.some(s => s.skillIds.includes(id)) : false
  if (used) fail('RESOURCE_IN_USE', '仍有目录或人员引用该资源')
  rows.splice(rows.indexOf(row), 1)
  context.audit(actor, 'CATALOG_DELETED', id, `逻辑删除 ${resource} ${row.name}；历史快照保留`)
  return { success: true }
}
export function updateWorker(context: MockContext, actor: string, id: string, dto: Schema['WorkerDTO']): Schema['WorkerVO'] {
  const worker = context.db.workers.find(w => w.id === id)
  if (!worker) fail('NOT_FOUND', '人员不存在', 404)
  if (dto.skillIds.some(s => !context.db.skills.some(k => k.id === s))) fail('NOT_FOUND', '技能不存在', 404)
  if (context.db.orders.some(({ order: o }) => o.workerId === id && !['COMPLETED', 'CANCELLED'].includes(o.status) && !o.service.skillIds.every(s => dto.skillIds.includes(s)))) fail('RESOURCE_IN_USE', '已有未完成订单依赖当前技能')
  Object.assign(worker, dto)
  const account = context.db.accounts.find(a => a.id === worker.accountId)!
  account.displayName = dto.displayName; account.phone = dto.phone
  context.audit(actor, 'WORKER_UPDATED', id, '更新人员资料、技能和允许派单状态')
  return worker
}
