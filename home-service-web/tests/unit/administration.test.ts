import { beforeEach, expect, it } from 'vitest'
import type { OperationId, Schema } from '../../src/api/types'
import { MockEngine } from '../../src/mock/engine'
import { createDatabase } from '../../src/mock/database'
import { DAY, HOUR, iso } from '../../src/utils/format'
import { validateSchema } from '../../src/mock/validation'
import schemas from '../../src/api/generated/schemas.json' with { type: 'json' }

const now = Date.parse('2026-10-03T09:00:00+08:00')
let engine: MockEngine, admin: string, customer: string
let sequence = 0
async function call(op: OperationId, token: string, body?: unknown, id?: string) {
  return (await engine.handle(op, { token, body, id, idempotencyKey: `admin_test_${++sequence}` }))
    .data
}
beforeEach(async () => {
  engine = new MockEngine(createDatabase(now), () => now)
  admin = (
    (await engine.handle('adminLogin', { body: { username: 'admin', password: 'Demo12345' } }))
      .data as Schema['LoginVO']
  ).accessToken
  customer = (
    (
      await engine.handle('customerLogin', {
        body: { username: 'customer', password: 'Demo12345' },
      })
    ).data as Schema['LoginVO']
  ).accessToken
})

it('契约中的示例均符合对应Schema', () => {
  for (const [name, schema] of Object.entries(schemas))
    if ('example' in schema) expect(() => validateSchema(name, schema.example), name).not.toThrow()
})

it('注册及账号资料维护，拒绝未知字段、空白名称、重复账号', async () => {
  const dto = {
    username: 'new_customer',
    password: 'NewPassword1',
    displayName: '新客户',
    phone: '13800000007',
  }
  const account = (await call('customerRegister', '', dto)) as Schema['AccountVO']
  expect(account).not.toHaveProperty('password')
  expect(engine.db.passwords[account.id]).not.toBe(dto.password)
  await expect(call('customerRegister', '', dto)).rejects.toMatchObject({ code: 'USERNAME_EXISTS' })
  await expect(
    call('customerRegister', '', { ...dto, username: 'another', role: 'ADMIN' }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  await expect(
    call('updateAccountProfile', admin, { displayName: '  ', phone: dto.phone }, account.id),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  const updated = (await call(
    'updateAccountProfile',
    admin,
    { displayName: '修改称呼', phone: dto.phone },
    account.id,
  )) as Schema['AccountVO']
  expect(updated.displayName).toBe('修改称呼')
})

it('地址默认唯一、区域校验与订单快照隔离', async () => {
  const { id: _id, customerId: _customerId, ...address } = engine.db.addresses[0]
  const second = (await call('createAddress', customer, {
    ...address,
    detail: '另一个虚构地址',
    isDefault: true,
  })) as Schema['AddressVO']
  expect(engine.db.addresses.filter((a) => a.isDefault)).toHaveLength(1)
  const historicDetail = engine.db.orders[0].order.address.detail
  await call('updateAddress', customer, { ...address, detail: '修改虚构地址' }, '401')
  expect(engine.db.orders[0].order.address.detail).toBe(historicDetail)
  await expect(
    call('createAddress', customer, { ...address, cityCode: '440300' }),
  ).rejects.toMatchObject({ code: 'OUTSIDE_SERVICE_AREA' })
  await expect(
    call('createAddress', customer, { ...address, longitude: 113.2, latitude: null }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  await call('deleteAddress', customer, undefined, second.id)
  expect(engine.db.addresses[0].isDefault).toBe(true)
})

it('目录新增更新与归档删除、引用保护和维修优惠限制', async () => {
  const category = (await call('createAdminCategory', admin, {
    name: '可维护演示分类',
    sort: 3,
    status: 'ON_SHELF',
  })) as Schema['CategoryVO']
  const item = (await call('createAdminServiceItem', admin, {
    categoryId: category.id,
    name: '演示清洁项目',
    serviceKind: 'CLEANING',
    description: '固定范围',
    status: 'ON_SHELF',
  })) as Schema['ServiceItemVO']
  const skill = (await call('createAdminSkill', admin, {
    name: '演示技能',
    description: '说明',
  })) as Schema['SkillVO']
  const skuDto: Schema['SkuDTO'] = {
    itemId: item.id,
    name: '演示规格',
    standardPrice: '100.00',
    minimumOfferPrice: '80.00',
    durationMinutes: 60,
    unit: '次',
    skillIds: [skill.id],
    status: 'ON_SHELF',
    supportsOffer: true,
    description: '固定套餐',
    included: '指定内容',
    excluded: '其他内容',
    customerSuppliesParts: false,
  }
  const sku = (await call('createAdminSku', admin, skuDto)) as Schema['SkuVO']
  await call('updateAdminSkill', admin, { name: skill.name, description: '更新说明' }, skill.id)
  await call('updateAdminSku', admin, { ...skuDto, name: '新规格名称' }, sku.id)
  await expect(call('deleteAdminSkill', admin, undefined, skill.id)).rejects.toMatchObject({
    code: 'RESOURCE_IN_USE',
  })
  const { id: _id, ...itemDto } = item
  await expect(
    call('updateAdminServiceItem', admin, { ...itemDto, serviceKind: 'REPAIR' }, item.id),
  ).rejects.toMatchObject({ code: 'OFFER_NOT_SUPPORTED' })
  await expect(
    call('createAdminSku', admin, { ...skuDto, name: '维修违规优惠', itemId: '23' }),
  ).rejects.toMatchObject({ code: 'OFFER_NOT_SUPPORTED' })
  await call(
    'updateAdminCategory',
    admin,
    { name: '已改名演示分类', sort: 3, status: 'OFF_SHELF' },
    category.id,
  )
  expect(engine.db.skus.find((s) => s.id === sku.id)!.categoryName).toBe('已改名演示分类')
  await expect(call('deleteAdminCategory', admin, undefined, category.id)).rejects.toMatchObject({
    code: 'RESOURCE_IN_USE',
  })
  await call('updateAdminSku', admin, { ...skuDto, status: 'OFF_SHELF' }, sku.id)
  await call('deleteAdminSku', admin, undefined, sku.id)
  await call('updateAdminServiceItem', admin, { ...itemDto, status: 'OFF_SHELF' }, item.id)
  await call('deleteAdminServiceItem', admin, undefined, item.id)
  await call('deleteAdminCategory', admin, undefined, category.id)
  await call('deleteAdminSkill', admin, undefined, skill.id)
  expect(engine.db.archivedCatalog).toHaveLength(4)
})

it('新建人员默认无可用槽，首次排班后参与匹配；资料更新不覆盖订单', async () => {
  const dto: Schema['WorkerCreateDTO'] = {
    username: 'worker_new',
    password: 'Demo12345',
    displayName: '新人员',
    phone: '13800000008',
    cityCode: '440100',
    skillIds: ['31'],
    dispatchEnabled: true,
  }
  const worker = (await call('createWorker', admin, dto)) as Schema['WorkerVO']
  const token = (
    (
      await engine.handle('workerLogin', {
        body: { username: dto.username, password: dto.password },
      })
    ).data as Schema['LoginVO']
  ).accessToken
  const slots = (await engine.handle('listWorkerSlots', { token, query: { date: '2026-10-04' } }))
    .data as Schema['SlotVO'][]
  expect(slots.every((s) => s.status === 'NON_WORKING')).toBe(true)
  await call('updateSchedule', token, {
    intervals: [{ start: '08:00', end: '22:00' }],
    restWeekdays: [],
  })
  await call('claimOffer', token, { expectedPrice: '130.00', priceVersion: 1 }, '10002')
  const { username: _username, password: _password, ...profile } = dto
  await call('updateWorker', admin, { ...profile, dispatchEnabled: false }, worker.id)
  await expect(
    call('updateWorker', admin, { ...profile, skillIds: ['32'] }, worker.id),
  ).rejects.toMatchObject({ code: 'RESOURCE_IN_USE' })
  await expect(
    call('setAccountStatus', admin, { status: 'DISABLED' }, worker.accountId),
  ).rejects.toMatchObject({ code: 'RESOURCE_IN_USE' })
})

it('配置只影响新预约，已建订单截止时间不变', async () => {
  const original = engine.db.orders[0].order.paymentDeadline
  await call('updateSettings', admin, { earliestHours: 3, latestDays: 5 })
  expect(engine.db.orders[0].order.paymentDeadline).toBe(original)
  await expect(
    call('updateSettings', admin, { earliestHours: 24, latestDays: 1 }),
  ).rejects.toMatchObject({ code: 'CONFIG_CONFLICT' })
  await expect(
    call('createOrder', customer, {
      skuId: '301',
      addressId: '401',
      bookingType: 'STANDARD',
      startTime: iso(now + 2 * HOUR),
    }),
  ).rejects.toMatchObject({ code: 'BOOKING_WINDOW_INVALID' })
})

it('管理员取消与系统超时退款分别记录真实操作者类型，终态禁止重复取消', async () => {
  await call('cancelAdminOrder', admin, { reason: '测试异常' }, '10002')
  expect(
    engine.db.audits.find((a) => a.action === 'FULL_REFUND' && a.targetId === '10002'),
  ).toMatchObject({ actorType: 'USER', actorId: '1' })
  await expect(call('cancelAdminOrder', admin, { reason: '重复' }, '10002')).rejects.toMatchObject({
    code: 'STATE_CONFLICT',
  })
  engine.db.clockOffset = 2 * HOUR
  await engine.handle('customerListOrders', { token: customer })
  const refund = engine.db.audits.find((a) => a.action === 'FULL_REFUND' && a.targetId === '10003')!
  expect(refund.actorType).toBe('SYSTEM')
  expect(refund).not.toHaveProperty('actorId')
})

it('幂等语义不依赖JSON键顺序，过期令牌不能重放成功响应', async () => {
  const idempotencyKey = 'stable_key_123456'
  const body = {
    newPrice: '135.00',
    expectedPrice: '130.00',
    priceVersion: 1,
    confirmSimulatedPayment: true,
  }
  const first = await engine.handle('changeOffer', {
    token: customer,
    id: '10002',
    body,
    idempotencyKey,
  })
  const reordered = {
    confirmSimulatedPayment: true,
    priceVersion: 1,
    expectedPrice: '130.00',
    newPrice: '135.00',
  }
  expect(
    await engine.handle('changeOffer', {
      token: customer,
      id: '10002',
      body: reordered,
      idempotencyKey,
    }),
  ).toEqual(first)
  engine.db.clockOffset = 2 * DAY
  await expect(
    engine.handle('changeOffer', { token: customer, id: '10002', body, idempotencyKey }),
  ).rejects.toMatchObject({ code: 'TOKEN_EXPIRED' })
})
