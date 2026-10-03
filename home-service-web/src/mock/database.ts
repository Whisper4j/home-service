import type { Schema } from '../api/types'
import { dateOf, DAY, HOUR, iso, tomorrowMorning } from '../utils/format'

export interface StoredOrder {
  order: Schema['OrderVO']
  startCode: string
  lastDispatchAt?: number
}
export interface Database {
  version: 1
  sequence: number
  clockOffset: number
  accounts: Schema['AccountVO'][]
  passwords: Record<string, string>
  workers: Schema['WorkerVO'][]
  categories: Schema['CategoryVO'][]
  items: Schema['ServiceItemVO'][]
  skus: Schema['SkuVO'][]
  skills: Schema['SkillVO'][]
  addresses: (Schema['AddressVO'] & { customerId: string })[]
  schedules: Record<string, Schema['ScheduleVO']>
  leaves: (Schema['LeaveVO'] & { workerId: string })[]
  orders: StoredOrder[]
  histories: Record<string, Schema['OrderHistoryVO']>
  attempts: Schema['DispatchAttemptVO'][]
  audits: Schema['AuditVO'][]
  sessions: Record<string, { accountId: string; expiresAt: number }>
  idempotency: Record<string, { fingerprint: string; response: unknown }>
  settings: Schema['SettingsDTO']
  archivedCatalog?: { resource: string; record: unknown }[]
}
export const regions: Schema['RegionVO'][] = [
  {
    provinceCode: '440000',
    provinceName: '广东省',
    cityCode: '440100',
    cityName: '广州市',
    districts: [
      ['440103', '荔湾区'],
      ['440104', '越秀区'],
      ['440105', '海珠区'],
      ['440106', '天河区'],
      ['440111', '白云区'],
      ['440112', '黄埔区'],
      ['440113', '番禺区'],
      ['440114', '花都区'],
      ['440115', '南沙区'],
      ['440117', '从化区'],
      ['440118', '增城区'],
    ].map(([code, name]) => ({ code, name })),
  },
]
export function bookingRules(db: Database): Schema['BookingRulesVO'] {
  return {
    cityCode: '440100',
    workStart: '08:00',
    workEnd: '22:00',
    slotMinutes: 30,
    ...db.settings,
    offerLeadHours: 12,
    offerWaitMinutes: 120,
    offerSafetyHours: 6,
    paymentTimeoutMinutes: 15,
    dispatchWaitMinutes: 5,
    dispatchScanSeconds: 30,
    standardBufferMinutes: 120,
    offerBufferMinutes: 60,
    autoConfirmHours: 24,
    priceStep: '5.00',
  }
}
export function serviceSnapshot(sku: Schema['SkuVO']): Schema['ServiceSnapshotVO'] {
  return {
    categoryName: sku.categoryName,
    itemName: sku.itemName,
    skuName: sku.name,
    standardPrice: sku.standardPrice,
    minimumOfferPrice: sku.minimumOfferPrice,
    durationMinutes: sku.durationMinutes,
    unit: sku.unit,
    skillIds: [...sku.skillIds],
    description: sku.description,
    included: sku.included,
    excluded: sku.excluded,
    customerSuppliesParts: sku.customerSuppliesParts,
  }
}
export function createDatabase(now = Date.now()): Database {
  const accounts: Schema['AccountVO'][] = [
    {
      id: '101',
      username: 'customer',
      displayName: '演示客户',
      role: 'CUSTOMER',
      status: 'ENABLED',
      phone: '13800000001',
    },
    {
      id: '102',
      username: 'customer2',
      displayName: '另一位演示客户',
      role: 'CUSTOMER',
      status: 'ENABLED',
      phone: '13800000002',
    },
    {
      id: '201',
      username: 'worker',
      displayName: '清洁人员甲',
      role: 'WORKER',
      status: 'ENABLED',
      phone: '13800000003',
    },
    {
      id: '202',
      username: 'worker2',
      displayName: '清洁人员乙',
      role: 'WORKER',
      status: 'ENABLED',
      phone: '13800000004',
    },
    {
      id: '203',
      username: 'repair',
      displayName: '维修人员丙',
      role: 'WORKER',
      status: 'ENABLED',
      phone: '13800000005',
    },
    {
      id: '1',
      username: 'admin',
      displayName: '平台管理员',
      role: 'ADMIN',
      status: 'ENABLED',
      phone: '13800000006',
    },
  ]
  const categories: Schema['CategoryVO'][] = [
    { id: '11', name: '清洁', sort: 1, status: 'ON_SHELF' },
    { id: '12', name: '维修', sort: 2, status: 'ON_SHELF' },
  ]
  const items: Schema['ServiceItemVO'][] = [
    ['21', '11', '日常清洁'],
    ['22', '11', '深度清洁'],
    ['23', '12', '管道与卫浴'],
    ['24', '12', '电气与灯具'],
    ['25', '12', '家电维护'],
  ].map(([id, categoryId, name]) => ({
    id,
    categoryId,
    name,
    serviceKind: categoryId === '11' ? 'CLEANING' : 'REPAIR',
    description: `${name}：服务边界以规格说明为准`,
    status: 'ON_SHELF',
  }))
  const skills: Schema['SkillVO'][] = [
    { id: '31', name: '住宅清洁', description: '日常与深度清洁' },
    { id: '32', name: '管道卫浴维修', description: '预先确定范围的卫浴维修' },
    { id: '33', name: '电气家电维护', description: '常规灯具与家电维护' },
  ]
  const specs = [
    ['301', '21', '日常清洁 2 小时', '160.00', '130.00', 120, '31'],
    ['302', '21', '日常清洁 3 小时', '240.00', '195.00', 180, '31'],
    ['303', '21', '日常清洁 4 小时', '320.00', '260.00', 240, '31'],
    ['304', '22', '深度清洁 60㎡以内', '300.00', '240.00', 180, '31'],
    ['305', '22', '深度清洁 61—100㎡', '450.00', '360.00', 240, '31'],
    ['306', '23', '疏通马桶', '150.00', '150.00', 60, '32'],
    ['307', '23', '维修马桶进水阀', '120.00', '120.00', 60, '32'],
    ['308', '23', '维修水龙头密封件', '100.00', '100.00', 60, '32'],
    ['309', '23', '更换同规格水龙头', '100.00', '100.00', 60, '32'],
    ['310', '24', '更换灯泡', '80.00', '80.00', 30, '33'],
    ['311', '24', '更换普通灯具', '120.00', '120.00', 60, '33'],
    ['312', '24', '更换同规格保险丝', '90.00', '90.00', 30, '33'],
    ['313', '25', '壁挂空调清洗', '130.00', '130.00', 90, '33'],
  ] as const
  const skus: Schema['SkuVO'][] = specs.map(
    ([id, itemId, name, standardPrice, minimumOfferPrice, durationMinutes, skillId]) => {
      const item = items.find((i) => i.id === itemId)!
      const clean = item.categoryId === '11'
      return {
        id,
        itemId,
        categoryId: item.categoryId,
        categoryName: categories.find((c) => c.id === item.categoryId)!.name,
        itemName: item.name,
        name,
        standardPrice,
        minimumOfferPrice,
        durationMinutes,
        skillIds: [skillId],
        unit: '次',
        status: 'ON_SHELF',
        supportsOffer: clean,
        description: clean
          ? '已入住住宅固定套餐，数量为1。深度清洁不包含开荒保洁。'
          : '固定服务范围，无法预先确定的故障检测不在本规格内。',
        included: clean ? '地面、可触及表面及厨卫清洁' : '指定项目人工操作与完成检查',
        excluded: clean ? '高空外窗、装修开荒、危险搬运' : '配件费用、隐蔽线路改造、现场加价',
        customerSuppliesParts: !clean,
      }
    },
  )
  const address: Schema['AddressVO'] = {
    id: '401',
    contactName: '演示联系人',
    contactPhone: '13800000001',
    provinceCode: '440000',
    provinceName: '广东省',
    cityCode: '440100',
    cityName: '广州市',
    districtCode: '440106',
    districtName: '天河区',
    detail: '演示路1号（虚构地址）',
    longitude: null,
    latitude: null,
    isDefault: true,
  }
  const db: Database = {
    version: 1,
    sequence: 20000,
    clockOffset: 0,
    accounts,
    passwords: {},
    categories,
    items,
    skus,
    skills,
    addresses: [{ ...address, customerId: '101' }],
    workers: accounts
      .filter((a) => a.role === 'WORKER')
      .map((a) => ({
        id: a.id,
        accountId: a.id,
        username: a.username,
        displayName: a.displayName,
        phone: a.phone,
        status: a.status,
        cityCode: '440100',
        skillIds: a.id === '203' ? ['32', '33'] : ['31'],
        dispatchEnabled: true,
      })),
    schedules: {},
    leaves: [],
    orders: [],
    histories: {},
    attempts: [],
    audits: [],
    sessions: {},
    idempotency: {},
    settings: { earliestHours: 2, latestDays: 7 },
  }
  for (const w of db.workers)
    db.schedules[w.id] = {
      configured: true,
      intervals: [{ start: '08:00', end: '22:00' }],
      restWeekdays: [],
    }
  const { id: _addressId, ...snapshot } = address
  for (let index = 0; index < 27; index++) {
    const id = String(10001 + index)
    const active = index < 3
    const offer = index === 1 || index === 2
    const leadDays = tomorrowMorning(now) - now >= 12 * HOUR ? 1 : 2
    const start = active
      ? tomorrowMorning(now, offer ? leadDays + (index === 2 ? 1 : 0) : 1)
      : tomorrowMorning(now - (index + 1) * DAY, 0)
    const sku = skus[0]
    const order: Schema['OrderVO'] = {
      id,
      customerId: '101',
      skuId: sku.id,
      bookingType: offer ? 'OFFER' : 'STANDARD',
      status: active
        ? offer
          ? 'WAITING_ACCEPTANCE'
          : 'PENDING_PAYMENT'
        : index % 3 === 0
          ? 'CANCELLED'
          : 'COMPLETED',
      paymentStatus: active ? (offer ? 'PAID' : 'UNPAID') : index % 3 === 0 ? 'REFUNDED' : 'PAID',
      dispatchStatus: active ? 'NOT_REQUIRED' : 'SUCCEEDED',
      service: serviceSnapshot(sku),
      address: snapshot,
      startTime: iso(start),
      endTime: iso(start + 2 * HOUR),
      bufferEndTime: iso(start + (offer ? 3 : 4) * HOUR),
      createdAt: iso(active ? now : start - DAY),
      paymentDeadline: iso(active ? now + HOUR / 4 : start - DAY + HOUR / 4),
      ...(offer ? { offerDeadline: iso(now + 2 * HOUR) } : {}),
      currentPrice: offer ? '130.00' : '160.00',
      priceVersion: 1,
      remark: '可操作演示数据',
      reviewed: false,
      ...(!active
        ? {
            dealPrice: '160.00',
            workerId: '201',
            workerName: '清洁人员甲',
            ...(index % 3 === 0 ? { cancellationReason: '演示历史取消' } : {}),
          }
        : {}),
    }
    db.orders.push({ order, startCode: String(120000 + index) })
    db.histories[id] = {
      payments:
        order.paymentStatus === 'UNPAID'
          ? []
          : [
              {
                id: String(11000 + index),
                orderId: id,
                businessNo: `DEMO_PAY_${id}`,
                type: 'PAYMENT',
                amount: order.currentPrice,
                createdAt: order.createdAt,
              },
              ...(order.paymentStatus === 'REFUNDED'
                ? [
                    {
                      id: String(12000 + index),
                      orderId: id,
                      businessNo: `DEMO_REFUND_${id}`,
                      type: 'FULL_REFUND' as const,
                      amount: order.currentPrice,
                      createdAt: iso(start - HOUR),
                    },
                  ]
                : []),
            ],
      priceHistory: [],
      assignments: active
        ? []
        : [
            {
              id: String(13000 + index),
              orderId: id,
              workerId: '201',
              workerName: '清洁人员甲',
              bookingType: 'STANDARD',
              status: order.status === 'CANCELLED' ? 'RELEASED' : 'FINISHED',
              assignedAt: iso(start - DAY),
            },
          ],
    }
  }
  // 种子日期随启动当天滚动，不会随着日历推进变成不可操作的过期数据。
  db.audits.push({
    id: '14001',
    actorType: 'SYSTEM',
    action: 'DEMO_INITIALIZED',
    targetId: '1',
    detail: `初始化 ${dateOf(now)} 虚构演示数据`,
    createdAt: iso(now),
  })
  return db
}
