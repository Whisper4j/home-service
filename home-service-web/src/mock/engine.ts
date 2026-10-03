import type { OperationId, Schema } from '../api/types'
import { routes } from '../api/generated/routes'
import { fail } from '../api/errors'
import { DAY, HOUR, iso } from '../utils/format'
import { MockContext } from './context'
import { bookingRules, createDatabase, regions } from './database'
import { validateSchema } from './validation'
import { clientEntries, matchesEntry } from '../utils/clientEntries'
import { authorizeImage, inspectImage, uploadImage } from './images'
import { canAssign, createLeave, daySlots, updateSchedule } from './scheduling'
import {
  assertPrice,
  assignOrder,
  cancelOrder,
  changeOffer,
  completeOrder,
  createOrder,
  offerView,
  payOrder,
  runDueJobs,
} from './orders'
import {
  catalogRows,
  deleteCatalog,
  updateWorker,
  writeCatalog,
  type CatalogResource,
} from './administration'

export interface MockRequest {
  id?: string
  body?: unknown
  file?: File
  query?: Record<string, unknown>
  token?: string
  idempotencyKey?: string
}
import { compareIds, compareWorkerOrders, workerCalendar, workerStatistics } from './worker'
const terminal = ['COMPLETED', 'CANCELLED']
function normalize(value: unknown, field = ''): unknown {
  if (typeof value === 'string') return field === 'password' ? value : value.trim()
  if (Array.isArray(value)) return value.map((item) => normalize(item))
  if (value && typeof value === 'object')
    return Object.fromEntries(
      Object.entries(value)
        .sort(([a], [b]) => a.localeCompare(b))
        .map(([name, data]) => [name, normalize(data, name)]),
    )
  return value
}
async function digest(password: string): Promise<string> {
  // 仅Mock浏览器存储摘要；正式密码校验始终由后端BCrypt处理。
  const result = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(password))
  return Array.from(new Uint8Array(result), (b) => b.toString(16).padStart(2, '0')).join('')
}
export class MockEngine extends MockContext {
  constructor(db = createDatabase(), clock: () => number = Date.now) {
    super(db, clock)
  }
  accountFor(token?: string): Schema['AccountVO'] {
    const session = token ? this.db.sessions[token] : undefined
    if (!session) fail('UNAUTHENTICATED', '请先登录', 401)
    if (this.now >= session.expiresAt) fail('TOKEN_EXPIRED', '登录已过期，请重新登录', 401)
    const account = this.db.accounts.find((a) => a.id === session.accountId)!
    if (account.status === 'DISABLED') fail('ACCOUNT_DISABLED', '账号已禁用', 403)
    return account
  }
  async handle(
    operationId: OperationId,
    request: MockRequest = {},
  ): Promise<{ code: 'SUCCESS'; message: string; data: unknown }> {
    request = {
      ...request,
      body: normalize(request.body),
      query: normalize(request.query) as MockRequest['query'],
    }
    const route = routes[operationId]
    const role = route.path.split('/')[1].toUpperCase()
    const account = route.anonymous ? undefined : this.accountFor(request.token)
    if (account && account.role !== role) fail('FORBIDDEN', '当前账号无权访问该端', 403)
    if (route.path.includes('{id}')) validateSchema('Id', request.id)
    if (route.input) validateSchema(route.input, request.body)
    if (route.query) validateSchema(route.query, request.query || {})
    const query = request.query || {}
    if (query.from && query.to && String(query.from) > String(query.to))
      fail('VALIDATION_ERROR', '开始日期不能晚于结束日期', 400)
    if (
      route.idempotent &&
      (!request.idempotencyKey || !/^[A-Za-z0-9_-]{8,128}$/.test(request.idempotencyKey))
    )
      fail('VALIDATION_ERROR', '缺少有效的 Idempotency-Key', 400)
    const key = `${account?.id || 'anonymous'}:${route.method}:${route.path}:${request.id || ''}:${request.idempotencyKey}`
    const fingerprint = route.upload
      ? `${request.file?.type}:${await inspectImage(request.file)}`
      : JSON.stringify(request.body || {})
    const cached = this.db.idempotency[key]
    if (route.idempotent && cached) {
      if (cached.fingerprint !== fingerprint)
        fail('IDEMPOTENCY_CONFLICT', '相同幂等键不能用于不同请求内容')
      return structuredClone(cached.response) as { code: 'SUCCESS'; message: string; data: unknown }
    }
    runDueJobs(this)
    const before = structuredClone(this.db),
      eventCount = this.events.length
    try {
      const data = operationId.endsWith('Login')
        ? await this.login(role, request.body as Schema['LoginDTO'])
        : operationId === 'customerRegister'
          ? await this.register(request.body as Schema['RegisterDTO'])
          : operationId === 'createWorker'
            ? await this.createWorker(account!.id, request.body as Schema['WorkerCreateDTO'])
            : this.dispatch(operationId, request, account)
      const response = {
        code: 'SUCCESS' as const,
        message: '操作成功',
        data: structuredClone(data),
      }
      if (route.output) validateSchema(route.output, response)
      if (route.idempotent)
        this.db.idempotency[key] = { fingerprint, response: structuredClone(response) }
      return response
    } catch (error) {
      this.db = before
      this.events.splice(eventCount)
      throw error
    }
  }
  private async login(role: string, dto: Schema['LoginDTO']): Promise<Schema['LoginVO']> {
    const account = this.db.accounts.find((a) => a.username === dto.username && a.role === role)
    if (
      !account ||
      (this.db.passwords[account.id]
        ? this.db.passwords[account.id] !== (await digest(dto.password))
        : dto.password !== 'Demo12345')
    )
      fail('INVALID_CREDENTIALS', '用户名、密码或登录角色不正确', 401)
    if (account.status !== 'ENABLED') fail('ACCOUNT_DISABLED', '账号已禁用', 403)
    const accessToken = `mock.${crypto.randomUUID()}`
    const expiresAt = this.now + 48 * HOUR
    this.db.sessions[accessToken] = { accountId: account.id, expiresAt }
    return { accessToken, tokenType: 'Bearer', expiresAt: iso(expiresAt), account }
  }
  private async register(dto: Schema['RegisterDTO']): Promise<Schema['AccountVO']> {
    if (this.db.accounts.some((a) => a.username === dto.username))
      fail('USERNAME_EXISTS', '用户名已存在')
    const account: Schema['AccountVO'] = {
      id: this.nextId(),
      username: dto.username,
      displayName: dto.displayName,
      phone: dto.phone,
      role: 'CUSTOMER',
      status: 'ENABLED',
    }
    this.db.passwords[account.id] = await digest(dto.password)
    this.db.accounts.push(account)
    return account
  }
  private async createWorker(
    actor: string,
    dto: Schema['WorkerCreateDTO'],
  ): Promise<Schema['WorkerVO']> {
    if (dto.skillIds.some((id) => !this.db.skills.some((s) => s.id === id)))
      fail('NOT_FOUND', '技能不存在', 404)
    const account = await this.register({
      username: dto.username,
      password: dto.password,
      displayName: dto.displayName,
      phone: dto.phone,
    })
    account.role = 'WORKER'
    const { password: _password, ...profile } = dto
    const worker: Schema['WorkerVO'] = {
      ...profile,
      id: account.id,
      accountId: account.id,
      status: 'ENABLED',
    }
    this.db.workers.push(worker)
    this.db.schedules[worker.id] = { configured: false, intervals: [], restWeekdays: [] }
    this.audit(actor, 'WORKER_CREATED', worker.id, `创建人员 ${worker.displayName}`)
    return worker
  }
  private page<T>(
    rows: T[],
    query: Record<string, unknown>,
  ): { list: T[]; total: number; pages: number } {
    const pageNo = Number(query.pageNo || 1),
      pageSize = Number(query.pageSize || 20)
    return {
      list: rows.slice((pageNo - 1) * pageSize, pageNo * pageSize),
      total: rows.length,
      pages: Math.ceil(rows.length / pageSize),
    }
  }
  private ownOrder(id: string, account: Schema['AccountVO']): Schema['OrderVO'] {
    const order = this.db.orders.find((s) => s.order.id === id)?.order
    if (
      !order ||
      (account.role === 'CUSTOMER' && order.customerId !== account.id) ||
      (account.role === 'WORKER' && order.workerId !== account.id)
    )
      fail('NOT_FOUND', '订单不存在或不可访问', 404)
    return order
  }
  private address(dto: Schema['AddressDTO']): void {
    const region = regions[0]
    if (
      dto.provinceCode !== region.provinceCode ||
      dto.provinceName !== region.provinceName ||
      dto.cityCode !== region.cityCode ||
      dto.cityName !== region.cityName ||
      !region.districts.some((d) => d.code === dto.districtCode && d.name === dto.districtName)
    )
      fail('OUTSIDE_SERVICE_AREA', '仅支持广东省广州市，区编码和名称需一致', 422)
    if ((dto.latitude === null) !== (dto.longitude === null))
      fail('VALIDATION_ERROR', '经纬度应同时填写或同时留空', 400)
  }
  private dispatch(op: OperationId, r: MockRequest, account?: Schema['AccountVO']): unknown {
    const route = routes[op],
      id = r.id!,
      q = r.query || {},
      body = r.body
    const actor = account?.id || '1'
    if (op.endsWith('CurrentAccount')) return account
    if (op === 'updateCustomerProfile') {
      Object.assign(account!, body)
      return account
    }
    if (op === 'listClientEntries')
      return clientEntries.map((entry) => {
        const sku = this.db.skus.find((s) => s.clientEntryCode === entry.code)
        const item = this.db.items.find((i) => i.id === sku?.itemId)
        return sku &&
          item &&
          this.visibleSku(sku) &&
          matchesEntry(entry.code, item.serviceKind, sku.durationMinutes)
          ? { code: entry.code, available: true, sku }
          : {
              code: entry.code,
              available: false,
              unavailableReason: '该服务暂不可预约，请稍后再来',
            }
      })
    if (op === 'uploadSceneImage') return uploadImage(this, actor, r.file!)
    if (op.endsWith('GetSceneImage'))
      return authorizeImage(this, account!, id, q.orderId as string | undefined)
    if (op === 'deleteSceneImage') {
      authorizeImage(this, account!, id)
      if (this.db.orders.some((s) => s.order.sceneImages.some((i) => i.id === id)))
        fail('RESOURCE_IN_USE', '历史订单图片不能删除')
      this.db.images = this.db.images.filter((i) => i.id !== id)
      return { success: true }
    }
    if (op === 'listServiceRegions') return regions
    if (op === 'getBookingRules' || op === 'getSettings') return bookingRules(this.db)
    if (op === 'updateSettings') {
      const dto = body as Schema['SettingsDTO']
      if (dto.earliestHours >= dto.latestDays * 24)
        fail('CONFIG_CONFLICT', '预约最早时间必须小于最远时间')
      this.db.settings = dto
      this.audit(actor, 'SETTINGS_UPDATED', '1', JSON.stringify(dto))
      return bookingRules(this.db)
    }
    if (op === 'getCustomerSku') {
      const sku = this.db.skus.find((s) => s.id === id)
      if (!sku || !this.visibleSku(sku)) fail('NOT_FOUND', '规格不存在或已下架', 404)
      return sku
    }
    const resource = route.path.split('/')[2] as CatalogResource
    if (['categories', 'service-items', 'skus', 'skills'].includes(resource)) {
      if (route.method === 'DELETE') return deleteCatalog(this, actor, resource, id)
      if (route.method !== 'GET') return writeCatalog(this, actor, resource, id, body)
      let rows = catalogRows(this, resource)
      const customer = route.path.startsWith('/customer')
      if (customer && q.status && q.status !== 'ON_SHELF')
        fail('VALIDATION_ERROR', '客户仅能查询已上架数据', 400)
      rows = rows.filter(
        (row) =>
          (!q.keyword || row.name.includes(String(q.keyword))) &&
          (!q.status || !('status' in row) || row.status === q.status) &&
          (!q.categoryId || ('categoryId' in row && row.categoryId === q.categoryId)) &&
          (!q.itemId || ('itemId' in row && row.itemId === q.itemId)),
      )
      if (customer)
        rows = rows.filter(
          (row) =>
            'status' in row &&
            row.status === 'ON_SHELF' &&
            (resource === 'skus'
              ? this.visibleSku(row as Schema['SkuVO'])
              : resource === 'service-items'
                ? this.db.categories.find(
                    (c) => c.id === (row as Schema['ServiceItemVO']).categoryId,
                  )?.status === 'ON_SHELF'
                : true),
        )
      return this.page(rows, q)
    }
    if (op === 'listAddresses')
      return this.db.addresses
        .filter((a) => a.customerId === actor)
        .map(({ customerId: _customerId, ...a }) => a)
    if (op === 'createAddress' || op === 'updateAddress') {
      const dto = body as Schema['AddressDTO']
      this.address(dto)
      const existing = this.db.addresses.find((a) => a.id === id && a.customerId === actor)
      if (op === 'updateAddress' && !existing) fail('NOT_FOUND', '地址不存在', 404)
      const isDefault =
        dto.isDefault || !this.db.addresses.some((a) => a.customerId === actor && a.id !== id)
      if (isDefault)
        this.db.addresses
          .filter((a) => a.customerId === actor)
          .forEach((a) => {
            a.isDefault = false
          })
      const address = { id: existing?.id || this.nextId(), ...dto, isDefault }
      if (existing) Object.assign(existing, address)
      else this.db.addresses.push({ ...address, customerId: actor })
      const owned = this.db.addresses.filter((a) => a.customerId === actor)
      if (!owned.some((a) => a.isDefault)) owned.find((a) => a.id !== address.id)!.isDefault = true
      return address
    }
    if (op === 'deleteAddress') {
      const index = this.db.addresses.findIndex((a) => a.id === id && a.customerId === actor)
      if (index < 0) fail('NOT_FOUND', '地址不存在', 404)
      const [removed] = this.db.addresses.splice(index, 1)
      if (removed.isDefault) {
        const first = this.db.addresses.find((a) => a.customerId === actor)
        if (first) first.isDefault = true
      }
      return { success: true }
    }
    if (op === 'getWorkerProfile') return this.workerProfile(actor)
    if (op === 'updateWorkerContact') {
      const worker = this.db.workers.find((w) => w.accountId === actor)!
      worker.phone = (body as Schema['WorkerContactDTO']).phone
      account!.phone = worker.phone
      return this.workerProfile(actor)
    }
    if (op === 'getWorkerStatistics') return workerStatistics(this, actor)
    if (op === 'getWorkerCalendar') return workerCalendar(this, actor, String(q.month))
    if (op === 'getSchedule') return this.db.schedules[actor]
    if (op === 'updateSchedule') return updateSchedule(this, actor, body as Schema['ScheduleDTO'])
    if (op === 'listWorkerSlots' || op === 'getAdminWorkerSlots') {
      if (!this.db.workers.some((w) => w.id === (op === 'listWorkerSlots' ? actor : id)))
        fail('NOT_FOUND', '人员不存在', 404)
      return daySlots(this, op === 'listWorkerSlots' ? actor : id, String(q.date))
    }
    if (op === 'listLeaves')
      return this.page(
        this.db.leaves
          .filter((l) => l.workerId === actor)
          .map(({ workerId: _workerId, ...l }) => l),
        q,
      )
    if (op === 'createLeave') return createLeave(this, actor, body as Schema['LeaveDTO'])
    if (op === 'cancelLeave') {
      const leave = this.db.leaves.find((l) => l.id === id && l.workerId === actor)
      if (!leave) fail('NOT_FOUND', '请假不存在', 404)
      if (leave.status !== 'ACTIVE' || this.now >= Date.parse(leave.startTime))
        fail('STATE_CONFLICT', '仅尚未开始的生效请假可以撤销')
      leave.status = 'CANCELLED'
      return { success: true }
    }
    if (op === 'createOrder') return createOrder(this, actor, body as Schema['CreateOrderDTO'])
    if (op.endsWith('ListOrders')) {
      if (q.status && q.statuses) fail('VALIDATION_ERROR', 'status 与 statuses 不能同时提供', 400)
      const rows = this.db.orders
        .map((s) => s.order)
        .filter(
          (o) =>
            (account!.role === 'ADMIN' ||
              (account!.role === 'CUSTOMER' ? o.customerId === actor : o.workerId === actor)) &&
            (!q.keyword ||
              o.id.includes(String(q.keyword)) ||
              o.service.skuName.includes(String(q.keyword))) &&
            (!q.status || o.status === q.status) &&
            (!q.statuses || (q.statuses as string[]).includes(o.status)) &&
            (!q.bookingType || o.bookingType === q.bookingType) &&
            (!q.from || o.startTime.slice(0, 10) >= String(q.from)) &&
            (!q.to || o.startTime.slice(0, 10) <= String(q.to)),
        )
      if (account!.role === 'WORKER') rows.sort(compareWorkerOrders)
      return this.page(rows, q)
    }
    if (op === 'listEligibleOffers' || op === 'claimOffer') {
      const worker = this.db.workers.find((w) => w.accountId === actor)!
      if (op === 'listEligibleOffers')
        return this.page(
          this.db.orders
            .map((s) => s.order)
            .filter(
              (o) =>
                o.status === 'WAITING_ACCEPTANCE' &&
                canAssign(this, worker, o) &&
                (!q.keyword || o.service.skuName.includes(String(q.keyword))) &&
                (!q.from || o.startTime.slice(0, 10) >= String(q.from)) &&
                (!q.to || o.startTime.slice(0, 10) <= String(q.to)),
            )
            .map(offerView)
            .sort(
              (a, b) =>
                (q.sort === 'LATEST'
                  ? b.publishedAt.localeCompare(a.publishedAt)
                  : a.offerDeadline.localeCompare(b.offerDeadline) ||
                    a.publishedAt.localeCompare(b.publishedAt)) || compareIds(a.id, b.id),
            ),
          q,
        )
      const order = this.db.orders.find((s) => s.order.id === id)?.order
      if (!order || order.bookingType !== 'OFFER') fail('NOT_FOUND', '优惠订单不存在', 404)
      if (order.status === 'CANCELLED') fail('OFFER_CLOSED', '优惠订单已截止或关闭')
      if (order.status !== 'WAITING_ACCEPTANCE') fail('ORDER_TAKEN', '订单已被其他人员接单')
      const dto = body as Schema['ClaimOfferDTO']
      assertPrice(order, dto.expectedPrice, dto.priceVersion)
      if (!canAssign(this, worker, order))
        fail('WORKER_INELIGIBLE', '技能、城市、排班或时间槽不满足接单条件', 422)
      assignOrder(this, order, worker)
      return order
    }
    if (route.path.includes('/orders/{id}')) {
      const order = this.ownOrder(id, account!)
      if (op.endsWith('GetOrder')) return order
      if (op.endsWith('GetOrderHistory')) return this.db.histories[id]
      if (op === 'payOrder') return payOrder(this, order)
      if (op === 'changeOffer') return changeOffer(this, order, body as Schema['ChangeOfferDTO'])
      if (op === 'cancelCustomerOrder' || op === 'cancelAdminOrder') {
        if (
          terminal.includes(order.status) ||
          (op === 'cancelCustomerOrder' &&
            ![
              'PENDING_PAYMENT',
              'WAITING_DISPATCH',
              'WAITING_ACCEPTANCE',
              'PENDING_SERVICE',
            ].includes(order.status))
        )
          fail('STATE_CONFLICT', '当前状态不允许取消')
        cancelOrder(this, order, (body as Schema['CancelOrderDTO']).reason, actor)
        if (op === 'cancelAdminOrder')
          this.audit(actor, 'ADMIN_CANCELLED_ORDER', id, order.cancellationReason!)
        return order
      }
      if (op === 'getStartCode') {
        if (!['PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE'].includes(order.status))
          fail('STATE_CONFLICT', '当前状态不显示服务开始码')
        return { startCode: this.db.orders.find((s) => s.order.id === id)!.startCode }
      }
      if (op === 'confirmOrder') {
        if (order.status !== 'PENDING_CONFIRMATION') fail('STATE_CONFLICT', '订单尚未等待确认')
        completeOrder(this, order)
        return order
      }
      if (op === 'createReview') {
        if (order.status !== 'COMPLETED') fail('STATE_CONFLICT', '只有已完成订单可以评价')
        if (order.reviewed) fail('REVIEW_EXISTS', '每单只能评价一次')
        const review: Schema['ReviewVO'] = {
          id: this.nextId(),
          orderId: id,
          createdAt: iso(this.now),
          ...(body as Schema['ReviewDTO']),
        }
        this.db.histories[id].review = review
        order.reviewed = true
        return review
      }
      const transitions: Partial<
        Record<OperationId, [Schema['OrderStatus'], Schema['OrderStatus']]>
      > = {
        departOrder: ['PENDING_SERVICE', 'DEPARTED'],
        arriveOrder: ['DEPARTED', 'ARRIVED'],
        startOrder: ['ARRIVED', 'IN_SERVICE'],
        finishOrder: ['IN_SERVICE', 'PENDING_CONFIRMATION'],
      }
      const transition = transitions[op]
      if (transition) {
        if (order.status !== transition[0])
          fail('STATE_CONFLICT', '订单状态已变化，请刷新', 409, { currentStatus: order.status })
        if (op === 'startOrder') {
          if (
            (body as Schema['StartServiceDTO']).startCode !==
            this.db.orders.find((s) => s.order.id === id)!.startCode
          )
            fail('START_CODE_INVALID', '服务开始码不正确', 422)
          if (this.now < Date.parse(order.startTime))
            fail('BOOKING_WINDOW_INVALID', '尚未到预约开始时间', 422)
        }
        order.status = transition[1]
        if (op === 'finishOrder') order.confirmationDeadline = iso(this.now + DAY)
        this.event('ORDER_STATUS_CHANGED', order)
        return order
      }
    }
    if (op === 'listAccounts')
      return this.page(
        this.db.accounts.filter(
          (a) =>
            (!q.keyword ||
              a.username.includes(String(q.keyword)) ||
              a.displayName.includes(String(q.keyword))) &&
            (!q.role || a.role === q.role) &&
            (!q.status || a.status === q.status),
        ),
        q,
      )
    if (op === 'setAccountStatus' || op === 'updateAccountProfile') {
      const target = this.db.accounts.find((a) => a.id === id)
      if (!target) fail('NOT_FOUND', '账号不存在', 404)
      if (op === 'setAccountStatus') {
        const dto = body as Schema['AccountStatusDTO']
        if (target.role === 'ADMIN' && dto.status === 'DISABLED')
          fail('FORBIDDEN', '不能禁用初始化管理员', 403)
        if (
          dto.status === 'DISABLED' &&
          this.db.orders.some((s) => s.order.workerId === id && !terminal.includes(s.order.status))
        )
          fail('RESOURCE_IN_USE', '该人员尚有未完成分配，请先处理异常订单')
        target.status = dto.status
      } else Object.assign(target, body)
      const worker = this.db.workers.find((w) => w.accountId === id)
      if (worker)
        Object.assign(worker, {
          status: target.status,
          displayName: target.displayName,
          phone: target.phone,
        })
      this.audit(actor, op, id, '账号状态或资料变更')
      return target
    }
    if (op === 'listWorkers')
      return this.page(
        this.db.workers.filter(
          (w) =>
            (!q.keyword ||
              w.displayName.includes(String(q.keyword)) ||
              w.username.includes(String(q.keyword))) &&
            (!q.skillId || w.skillIds.includes(String(q.skillId))) &&
            (q.dispatchEnabled === undefined || w.dispatchEnabled === q.dispatchEnabled),
        ),
        q,
      )
    if (op === 'updateWorker') return updateWorker(this, actor, id, body as Schema['WorkerDTO'])
    if (op === 'listDispatchAttempts' || op === 'listPayments' || op === 'listAudits') {
      const rows =
        op === 'listDispatchAttempts'
          ? this.db.attempts
          : op === 'listPayments'
            ? Object.values(this.db.histories)
                .flatMap((h) => h.payments)
                .sort((a, b) => b.createdAt.localeCompare(a.createdAt))
            : this.db.audits
      return this.page(
        rows.filter(
          (row) =>
            (!q.orderId ||
              ('orderId' in row ? row.orderId === q.orderId : row.targetId === q.orderId)) &&
            (!q.keyword || JSON.stringify(row).includes(String(q.keyword))),
        ),
        q,
      )
    }
    fail('NOT_FOUND', `未实现的接口 ${op}`, 404)
  }
  private visibleSku(sku: Schema['SkuVO']): boolean {
    return (
      sku.status === 'ON_SHELF' &&
      this.db.items.find((i) => i.id === sku.itemId)?.status === 'ON_SHELF' &&
      this.db.categories.find((c) => c.id === sku.categoryId)?.status === 'ON_SHELF'
    )
  }
  private workerProfile(accountId: string): Schema['WorkerProfileVO'] {
    const worker = this.db.workers.find((w) => w.accountId === accountId)!
    return { ...worker, skills: this.db.skills.filter((s) => worker.skillIds.includes(s.id)) }
  }
}
