<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sessions } from '../../stores/session'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/CustomerFeedback.vue'
const route = useRoute(),
  router = useRouter()
const addresses = ref<Schema['AddressVO'][]>([]),
  regions = ref<Schema['RegionVO'][]>([]),
  editingId = ref('')
const empty = (): Schema['AddressDTO'] => ({
  contactName: sessions.customer?.account.displayName || '',
  contactPhone: sessions.customer?.account.phone || '',
  provinceCode: '440000',
  provinceName: '广东省',
  cityCode: '440100',
  cityName: '广州市',
  districtCode: '440106',
  districtName: '天河区',
  detail: '',
  latitude: null,
  longitude: null,
  isDefault: false,
})
const form = reactive(empty()),
  { busy, error, success, run } = useTask()
async function refreshData() {
  const [a, r] = await Promise.all([request('listAddresses'), request('listServiceRegions')])
  addresses.value = a
  regions.value = r
}
function load() {
  return run(refreshData)
}
function edit(address: Schema['AddressVO']) {
  const { id, ...dto } = address
  editingId.value = id
  Object.assign(form, dto)
  document.getElementById('address-form')?.scrollIntoView({ behavior: 'instant' })
}
function reset() {
  editingId.value = ''
  Object.assign(form, empty())
}
function setCoordinate(field: 'longitude' | 'latitude', event: Event) {
  const value = (event.target as HTMLInputElement).value
  form[field] = value === '' ? null : Number(value)
}
function save() {
  void run(
    async (key) => {
      const district = regions.value[0].districts.find((d) => d.code === form.districtCode)!
      form.districtName = district.name
      const saved = editingId.value
        ? await request('updateAddress', {
            id: editingId.value,
            body: { ...form },
            idempotencyKey: key,
          })
        : await request('createAddress', { body: { ...form }, idempotencyKey: key })
      if (
        typeof route.query.returnTo === 'string' &&
        route.query.returnTo.startsWith('/customer/booking/')
      ) {
        const target = router.resolve(route.query.returnTo)
        await router.replace({ path: target.path, query: { ...target.query, addressId: saved.id } })
        return
      }
      reset()
      await refreshData()
    },
    '地址已保存',
    JSON.stringify({ form, id: editingId.value }),
  )
}
function remove(id: string) {
  if (confirm('删除此地址？历史订单的地址快照会保留。'))
    void run(
      async (key) => {
        await request('deleteAddress', { id, idempotencyKey: key })
        await refreshData()
      },
      '地址已删除',
      id,
    )
}
useRefresh(load)
function makeDefault(address: Schema['AddressVO']) {
  const { id, ...dto } = address
  return run(
    async (key) => {
      await request('updateAddress', { id, body: { ...dto, isDefault: true }, idempotencyKey: key })
      await refreshData()
    },
    '默认地址已更新',
    `default:${id}`,
  )
}
</script>
<template>
  <h1>我的地址</h1>
  <p class="muted">
    默认地址只有一个。删除默认地址后，最早添加的剩余地址会设为默认。地址联系人不会覆盖个人资料和历史订单。
  </p>
  <Feedback :error="error" :success="success" :busy="busy" />
  <div class="grid">
    <article v-for="address in addresses" :key="address.id" class="panel">
      <h2>
        {{ address.contactName }}
        <span v-if="address.isDefault" class="badge">默认</span>
      </h2>
      <p>{{ address.contactPhone }}</p>
      <p>
        {{ address.provinceName }}{{ address.cityName }}{{ address.districtName
        }}{{ address.detail }}
      </p>
      <div class="actions">
        <button @click="edit(address)">编辑</button>
        <button v-if="!address.isDefault" :disabled="busy" @click="makeDefault(address)">
          设为默认
        </button>
        <button :disabled="busy" @click="remove(address.id)">删除</button>
      </div>
    </article>
  </div>
  <p v-if="!busy && !addresses.length" class="empty">还没有地址，请先添加。</p>
  <section id="address-form" class="panel">
    <h2>{{ editingId ? '编辑地址' : '新增地址' }}</h2>
    <form id="address-editor" @submit.prevent="save">
      <div class="form-grid">
        <label>
          联系人
          <input v-model.trim="form.contactName" required maxlength="40" />
        </label>
        <label>
          联系电话
          <input v-model.trim="form.contactPhone" required pattern="1[0-9]{10}" />
        </label>
        <label>
          服务区域
          <select v-model="form.districtCode">
            <option
              v-for="district in regions[0]?.districts"
              :key="district.code"
              :value="district.code"
            >
              广东省 / 广州市 / {{ district.name }}
            </option>
          </select>
        </label>
        <label>
          详细地址
          <input v-model.trim="form.detail" required maxlength="200" />
        </label>
        <label>
          经度（可空）
          <input
            :value="form.longitude"
            type="number"
            min="-180"
            max="180"
            step="any"
            @input="setCoordinate('longitude', $event)"
          />
        </label>
        <label>
          纬度（可空）
          <input
            :value="form.latitude"
            type="number"
            min="-90"
            max="90"
            step="any"
            @input="setCoordinate('latitude', $event)"
          />
        </label>
      </div>
      <label class="inline">
        <input v-model="form.isDefault" type="checkbox" />
        设为默认地址
      </label>
      <div class="actions">
        <button :disabled="busy">保存地址</button>
        <button type="button" @click="reset">清空 / 新增</button>
      </div>
    </form>
  </section>
</template>
