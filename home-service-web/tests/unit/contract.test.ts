import { readFileSync, existsSync } from 'node:fs'
import { resolve } from 'node:path'
import { parse } from 'yaml'
import { describe, expect, it } from 'vitest'
import { routes } from '../../src/api/generated/routes'
const spec = parse(readFileSync(resolve('../docs/api/openapi.yaml'), 'utf8'))
const schemas = spec.components.schemas
describe('正式接口职责约束', () => {
  it('入口没有固定业务枚举，SKU写入始终明确绑定意图', () => {
    expect(schemas.ClientEntryCode.enum).toBeUndefined()
    expect(schemas.ClientEntryCode.maxLength).toBe(64)
    expect(schemas.SkuDTO.required).toContain('clientEntryCode')
    expect(schemas.SkuDTO.properties.clientEntryCode.nullable).toBe(true)
    expect(schemas.SkuDTO.properties.clientEntryCode.enum).toBeUndefined()
    expect(schemas.ClientEntryVO.required).toEqual(
      expect.arrayContaining([
        'groupCode',
        'groupName',
        'groupSort',
        'name',
        'description',
        'sort',
        'available',
      ]),
    )
  })
  it('查询模型只包含对应资源的字段', () => {
    for (const [name, fields] of Object.entries({
      CategoryQuery: ['pageNo', 'pageSize', 'keyword', 'status'],
      ServiceItemQuery: ['pageNo', 'pageSize', 'keyword', 'categoryId', 'status'],
      SkuQuery: ['pageNo', 'pageSize', 'keyword', 'categoryId', 'itemId', 'status'],
      SkillQuery: ['pageNo', 'pageSize', 'keyword'],
    })) {
      expect(Object.keys(schemas[name].properties)).toEqual(fields)
    }
    expect(schemas.CatalogQuery).toBeUndefined()
    for (const [path, item] of Object.entries(spec.paths) as [
      string,
      { get?: { parameters?: { name: string }[]; 'x-query-schema'?: string } },
    ][]) {
      if (path.startsWith('/customer/') && item.get?.['x-query-schema']?.startsWith('Public'))
        expect(item.get.parameters?.map((p) => p.name)).not.toContain('status')
    }
    expect(routes.listAdminSkill.query).toBe('SkillQuery')
  })
  it('不保留浏览器历史数据专用报价规则', () => {
    expect(schemas.OfferPriceRule).toBeUndefined()
    expect(schemas.OrderVO.properties.offerPriceRule).toBeUndefined()
    expect(schemas.BookingRulesVO.properties.offerPriceRule).toBeUndefined()
    expect(schemas.BookingRulesVO.required).toContain('priceStep')
  })
  it('状态历史、释放原因和审计目标可解释', () => {
    expect(schemas.OrderHistoryVO.required).toContain('statusHistory')
    expect(schemas.OrderStatusHistoryVO.required).toContain('actorType')
    expect(schemas.AssignmentVO.properties).toHaveProperty('releasedAt')
    expect(schemas.AssignmentVO.properties).toHaveProperty('releaseReason')
    expect(schemas.AssignmentVO.properties).toHaveProperty('finishedAt')
    expect(schemas.AuditVO.required).toContain('targetType')
    expect(schemas.AuditVO.properties).toHaveProperty('orderId')
    expect(schemas.OrderVO.required).toContain('allowedActions')
  })
  it('客户端地址输入与历史快照隔离', () => {
    expect(schemas.AddressDTO.properties.longitude).toBeUndefined()
    expect(schemas.AddressDTO.properties.latitude).toBeUndefined()
    expect(schemas.OrderVO.properties.address.$ref).toBe(
      '#/components/schemas/OrderAddressSnapshotVO',
    )
  })
  it('工程没有浏览器业务数据库目录', () => {
    expect(existsSync(resolve('src/mock/transport.ts'))).toBe(false)
  })
})
