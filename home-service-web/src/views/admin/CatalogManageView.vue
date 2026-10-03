<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { request } from '../../api/client'
import { allPages } from '../../api/pagination'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
import { clientEntries } from '../../utils/clientEntries'
type Row = Schema['CategoryVO'] | Schema['ServiceItemVO'] | Schema['SkuVO'] | Schema['SkillVO']
type Resource = 'categories' | 'service-items' | 'skus' | 'skills'
const route = useRoute(),
  resource = computed(() => String(route.params.resource) as Resource)
const titles = {
  categories: '服务分类',
  'service-items': '服务项目',
  skus: '服务规格与价格',
  skills: '技能管理',
}
const rows = ref<Row[]>([]),
  total = ref(0),
  pages = ref(0),
  editing = ref(''),
  formOpen = ref(false)
const categories = ref<Schema['CategoryVO'][]>([]),
  items = ref<Schema['ServiceItemVO'][]>([]),
  skills = ref<Schema['SkillVO'][]>([])
const query = reactive({
  pageNo: 1,
  pageSize: 20,
  keyword: '',
  status: '' as Schema['CatalogStatus'] | '',
})
const category = reactive<Schema['CategoryDTO']>({ name: '', sort: 0, status: 'ON_SHELF' })
const item = reactive<Schema['ServiceItemDTO']>({
  categoryId: '',
  name: '',
  serviceKind: 'CLEANING',
  description: '',
  status: 'ON_SHELF',
})
const skill = reactive<Schema['SkillDTO']>({ name: '', description: '' })
const initialSku = (): Schema['SkuDTO'] => ({
  clientEntryCode: null,
  itemId: '',
  name: '',
  standardPrice: '160.00',
  minimumOfferPrice: '130.00',
  durationMinutes: 120,
  unit: '次',
  skillIds: [],
  status: 'ON_SHELF',
  supportsOffer: true,
  description: '',
  included: '',
  excluded: '',
  customerSuppliesParts: false,
})
const sku = reactive(initialSku()),
  { busy, error, success, run } = useTask()
async function refreshData() {
  if (!titles[resource.value]) throw Error('无效目录类型')
  const operations = {
    categories: 'listAdminCategory',
    'service-items': 'listAdminServiceItem',
    skus: 'listAdminSku',
    skills: 'listAdminSkill',
  } as const
  const page = await request(operations[resource.value], {
    query: {
      pageNo: query.pageNo,
      pageSize: query.pageSize,
      ...(query.keyword ? { keyword: query.keyword } : {}),
      ...(query.status && resource.value !== 'skills' ? { status: query.status } : {}),
    },
  })
  rows.value = page.list
  total.value = page.total
  pages.value = page.pages
  const [c, i, s] = await Promise.all([
    allPages((n) => request('listAdminCategory', { query: { pageNo: n, pageSize: 100 } })),
    allPages((n) => request('listAdminServiceItem', { query: { pageNo: n, pageSize: 100 } })),
    allPages((n) => request('listAdminSkill', { query: { pageNo: n, pageSize: 100 } })),
  ])
  categories.value = c
  items.value = i
  skills.value = s
}
function load() {
  return run(refreshData)
}
function search() {
  query.pageNo = 1
  void load()
}
function open(row?: Row) {
  editing.value = row?.id || ''
  formOpen.value = true
  if (resource.value === 'categories')
    Object.assign(
      category,
      row
        ? pick(row as Schema['CategoryVO'], ['name', 'sort', 'status'])
        : { name: '', sort: 0, status: 'ON_SHELF' },
    )
  if (resource.value === 'service-items')
    Object.assign(
      item,
      row
        ? pick(row as Schema['ServiceItemVO'], [
            'categoryId',
            'name',
            'serviceKind',
            'description',
            'status',
          ])
        : {
            categoryId: categories.value[0]?.id || '',
            name: '',
            serviceKind: 'CLEANING',
            description: '',
            status: 'ON_SHELF',
          },
    )
  if (resource.value === 'skills')
    Object.assign(
      skill,
      row ? pick(row as Schema['SkillVO'], ['name', 'description']) : { name: '', description: '' },
    )
  if (resource.value === 'skus') {
    Object.assign(sku, initialSku())
    if (row)
      for (const key of Object.keys(initialSku()) as (keyof Schema['SkuDTO'])[])
        Object.assign(sku, { [key]: (row as Schema['SkuVO'])[key] })
    sku.skillIds = [...sku.skillIds]
  }
}
function pick<T extends object, K extends keyof T>(object: T, keys: K[]): Pick<T, K> {
  return Object.fromEntries(keys.map((key) => [key, object[key]])) as Pick<T, K>
}
function save() {
  void run(
    async (key) => {
      const common = { id: editing.value || undefined, idempotencyKey: key }
      switch (resource.value) {
        case 'categories':
          await request(editing.value ? 'updateAdminCategory' : 'createAdminCategory', {
            ...common,
            body: { ...category },
          })
          break
        case 'service-items':
          await request(editing.value ? 'updateAdminServiceItem' : 'createAdminServiceItem', {
            ...common,
            body: { ...item },
          })
          break
        case 'skills':
          await request(editing.value ? 'updateAdminSkill' : 'createAdminSkill', {
            ...common,
            body: { ...skill },
          })
          break
        case 'skus':
          await request(editing.value ? 'updateAdminSku' : 'createAdminSku', {
            ...common,
            body: { ...sku, skillIds: [...sku.skillIds] },
          })
          break
      }
      formOpen.value = false
      await refreshData()
    },
    '已保存，历史订单快照不受影响',
    JSON.stringify({ resource: resource.value, id: editing.value, category, item, skill, sku }),
  )
}
function remove(row: Row) {
  if (!confirm(`确认删除“${row.name}”？存在当前引用时将拒绝删除。`)) return
  const operations = {
    categories: 'deleteAdminCategory',
    'service-items': 'deleteAdminServiceItem',
    skus: 'deleteAdminSku',
    skills: 'deleteAdminSkill',
  } as const
  void run(
    async (key) => {
      await request(operations[resource.value], { id: row.id, idempotencyKey: key })
      await refreshData()
    },
    '已删除，历史数据保留',
    row.id,
  )
}
useRefresh(load)
function changePage(value: number) {
  query.pageNo = value
  void load()
}
function resizePage(value: number) {
  query.pageSize = value
  query.pageNo = 1
  void load()
}
</script>
<template>
  <h1>{{ titles[resource] || '目录不存在' }}</h1>
  <p>分类、项目与规格由平台维护；目录变更不改写历史订单快照。</p>
  <form class="filters" @submit.prevent="search">
    <label>
      名称
      <input v-model.trim="query.keyword" />
    </label>
    <label v-if="resource !== 'skills'">
      状态
      <select v-model="query.status">
        <option value="">全部</option>
        <option value="ON_SHELF">已上架</option>
        <option value="OFF_SHELF">已下架</option>
      </select>
    </label>
    <button :disabled="busy">查询</button>
    <button type="button" @click="open()">新增</button>
  </form>
  <Feedback :error="error" :success="success" :busy="busy" />
  <section v-if="formOpen" class="panel">
    <h2>{{ editing ? '编辑' : '新增' }}{{ titles[resource] }}</h2>
    <form @submit.prevent="save">
      <template v-if="resource === 'categories'">
        <label>
          分类名称
          <input v-model.trim="category.name" required maxlength="60" />
        </label>
        <label>
          排序
          <input v-model.number="category.sort" type="number" min="0" max="9999" required />
        </label>
        <label>
          状态
          <select v-model="category.status">
            <option value="ON_SHELF">上架</option>
            <option value="OFF_SHELF">下架</option>
          </select>
        </label>
      </template>
      <template v-if="resource === 'service-items'">
        <label>
          所属分类
          <select v-model="item.categoryId" required>
            <option value="" disabled>选择分类</option>
            <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
          </select>
        </label>
        <label>
          项目名称
          <input v-model.trim="item.name" required maxlength="60" />
        </label>
        <label>
          业务性质
          <select v-model="item.serviceKind">
            <option value="CLEANING">清洁（可配置优惠）</option>
            <option value="REPAIR">维修（仅标准预约）</option>
            <option value="OTHER">其他（仅标准预约）</option>
          </select>
        </label>
        <label>
          服务说明
          <textarea v-model.trim="item.description" required maxlength="1000" />
        </label>
        <label>
          状态
          <select v-model="item.status">
            <option value="ON_SHELF">上架</option>
            <option value="OFF_SHELF">下架</option>
          </select>
        </label>
      </template>
      <template v-if="resource === 'skills'">
        <label>
          技能名称
          <input v-model.trim="skill.name" required maxlength="60" />
        </label>
        <label>
          技能说明
          <textarea v-model.trim="skill.description" required maxlength="300" />
        </label>
      </template>
      <template v-if="resource === 'skus'">
        <label>
          客户端固定入口绑定
          <select v-model="sku.clientEntryCode">
            <option :value="null">不绑定客户端入口</option>
            <option v-for="entry in clientEntries" :key="entry.code" :value="entry.code">
              {{ entry.title }}（{{ entry.code }}）
            </option>
          </select>
        </label>
        <p class="muted">
          每个入口仅绑定一个规格；已下架规格保留绑定，客户端显示暂不可预约。新增分类不会自动成为客户端入口。
        </p>
        <div class="form-grid">
          <label>
            所属项目
            <select v-model="sku.itemId" required>
              <option value="" disabled>选择项目</option>
              <option v-for="i in items" :key="i.id" :value="i.id">{{ i.name }}</option>
            </select>
          </label>
          <label>
            规格名称
            <input v-model.trim="sku.name" required maxlength="80" />
          </label>
          <label>
            标准价（两位小数）
            <input v-model="sku.standardPrice" required pattern="[0-9]+\.[0-9]{2}" />
          </label>
          <label>
            最低报价（不支持优惠时同标准价）
            <input v-model="sku.minimumOfferPrice" required pattern="[0-9]+\.[0-9]{2}" />
          </label>
          <label>
            预计时长（分钟）
            <input
              v-model.number="sku.durationMinutes"
              required
              type="number"
              min="30"
              max="720"
              step="30"
            />
          </label>
          <label>
            计价单位
            <input v-model.trim="sku.unit" required maxlength="20" />
          </label>
          <label>
            状态
            <select v-model="sku.status">
              <option value="ON_SHELF">上架</option>
              <option value="OFF_SHELF">下架</option>
            </select>
          </label>
        </div>
        <div class="check-group">
          <label class="inline">
            <input v-model="sku.supportsOffer" type="checkbox" />
            支持优惠预约（仅清洁项目）
          </label>
          <label class="inline">
            <input v-model="sku.customerSuppliesParts" type="checkbox" />
            客户自备配件
          </label>
        </div>
        <p>所需技能（至少一项）</p>
        <div class="check-group">
          <label v-for="s in skills" :key="s.id" class="inline">
            <input v-model="sku.skillIds" type="checkbox" :value="s.id" />
            {{ s.name }}
          </label>
        </div>
        <label>
          服务说明
          <textarea v-model.trim="sku.description" required maxlength="2000" />
        </label>
        <label>
          包含内容
          <textarea v-model.trim="sku.included" required maxlength="1000" />
        </label>
        <label>
          排除内容
          <textarea v-model.trim="sku.excluded" required maxlength="1000" />
        </label>
      </template>
      <div class="actions">
        <button :disabled="busy">保存</button>
        <button type="button" @click="formOpen = false">关闭编辑</button>
      </div>
    </form>
  </section>
  <div class="table-scroll">
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>名称</th>
          <th>信息</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in rows" :key="row.id">
          <td>{{ row.id }}</td>
          <td>{{ row.name }}</td>
          <td>
            <template v-if="'standardPrice' in row">
              {{ row.itemName }} · ¥{{ row.standardPrice }} · {{ row.durationMinutes }}分钟
              <br />
              {{ row.supportsOffer ? `最低报价 ¥${row.minimumOfferPrice}` : '仅标准预约' }}
            </template>
            <template v-else-if="'sort' in row">排序 {{ row.sort }}</template>
            <template v-else>{{ row.description }}</template>
          </td>
          <td>{{ 'status' in row ? label(row.status) : '—' }}</td>
          <td class="nowrap">
            <button :disabled="busy" @click="open(row)">编辑</button>
            <button :disabled="busy" @click="remove(row)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  <p v-if="!busy && !rows.length" class="empty">暂无数据。</p>
  <Pagination
    v-bind="query"
    :total="total"
    :pages="pages"
    @change="changePage"
    @resize="resizePage"
  />
</template>
