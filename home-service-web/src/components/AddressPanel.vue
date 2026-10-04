<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { request } from '../api/client'
import type { Schema } from '../api/types'
import { sessions } from '../stores/session'
import { useTask } from '../composables/useTask'
import ModalPanel from './ModalPanel.vue'
import Feedback from './Feedback.vue'
const props = defineProps<{ selecting?: boolean; add?: boolean; context?: string }>()
const emit = defineEmits<{ close: []; selected: [address: Schema['AddressVO']] }>()
const addresses = ref<Schema['AddressVO'][]>([]),
  regions = ref<Schema['RegionVO'][]>([])
const editing = ref(false),
  editingId = ref(''),
  baseline = ref(''),
  loader = useTask(),
  action = useTask()
const key = `address.draft.${sessions.customer?.account.id}.${props.context || 'me'}`
function empty(): Schema['AddressDTO'] {
  return {
    contactName: sessions.customer?.account.displayName || '',
    contactPhone: sessions.customer?.account.phone || '',
    provinceCode: '440000',
    provinceName: '广东省',
    cityCode: '440100',
    cityName: '广州市',
    districtCode: '440106',
    districtName: '天河区',
    detail: '',
    isDefault: false,
  }
}
function toDto(address: Schema['AddressVO']): Schema['AddressDTO'] {
  const { id: _id, longitude: _longitude, latitude: _latitude, ...dto } = address
  return dto
}
const form = reactive(empty())
const dirty = computed(() => editing.value && JSON.stringify(form) !== baseline.value)
watch(
  form,
  () => {
    if (dirty.value)
      sessionStorage.setItem(
        key,
        JSON.stringify({ form, editingId: editingId.value, baseline: baseline.value }),
      )
    else if (editing.value) sessionStorage.removeItem(key)
  },
  { deep: true, flush: 'sync' },
)
async function load() {
  await loader.run(async () => {
    const [list, serviceRegions] = await Promise.all([
      request('listAddresses'),
      request('listServiceRegions'),
    ])
    addresses.value = list
    regions.value = serviceRegions
  })
}
function edit(address?: Schema['AddressVO']) {
  editing.value = false
  editingId.value = address?.id || ''
  Object.assign(form, address ? toDto(address) : empty())
  baseline.value = JSON.stringify(form)
  editing.value = true
}
function backToList() {
  if (dirty.value && !confirm('有未保存的地址修改，是否放弃？')) return
  editing.value = false
  sessionStorage.removeItem(key)
}
async function save() {
  await action.run(
    async (idempotencyKey) => {
      const district = regions.value[0]?.districts.find((d) => d.code === form.districtCode)
      if (!district) throw Error('请选择服务区域')
      const body = { ...form, districtName: district.name }
      const saved = editingId.value
        ? await request('updateAddress', { id: editingId.value, body, idempotencyKey })
        : await request('createAddress', { body, idempotencyKey })
      editing.value = false
      sessionStorage.removeItem(key)
      if (props.selecting) emit('selected', saved)
      else await load()
    },
    '地址已保存',
    JSON.stringify({ form, id: editingId.value }),
  )
}
async function remove(id: string) {
  if (!confirm('删除此地址？历史订单地址不会改变。删除默认地址后，最早添加的剩余地址成为默认。'))
    return
  await action.run(
    async (idempotencyKey) => {
      await request('deleteAddress', { id, idempotencyKey })
      await load()
    },
    '地址已删除',
    id,
  )
}
async function makeDefault(address: Schema['AddressVO']) {
  await action.run(
    async (idempotencyKey) => {
      await request('updateAddress', {
        id: address.id,
        body: { ...toDto(address), isDefault: true },
        idempotencyKey,
      })
      await load()
    },
    '默认地址已更新',
    `default:${address.id}`,
  )
}
function discardDraft() {
  sessionStorage.removeItem(key)
}
function close() {
  discardDraft()
  emit('close')
}
onMounted(async () => {
  await load()
  try {
    const draft = JSON.parse(sessionStorage.getItem(key) || 'null')
    if (draft) {
      Object.assign(form, draft.form)
      editingId.value = draft.editingId
      baseline.value = draft.baseline
      editing.value = true
      return
    }
  } catch {
    sessionStorage.removeItem(key)
  }
  if (props.add) edit()
})
</script>
<template>
  <ModalPanel
    :title="editing ? (editingId ? '编辑地址' : '新增地址') : selecting ? '选择服务地址' : '地址簿'"
    expanded
    :dirty="dirty"
    :busy="action.busy.value"
    @discard="discardDraft"
    @close="close"
  >
    <Feedback :error="loader.error.value" :busy="loader.busy.value" />
    <button v-if="loader.error.value" @click="load">重新加载地址</button>
    <template v-if="!editing">
      <p class="muted">
        默认地址唯一，删除默认后最早添加的剩余地址成为默认。地址联系人与个人资料、本次订单联系人独立。
      </p>
      <article v-for="address in addresses" :key="address.id" class="panel">
        <h3>
          {{ address.contactName }}
          <span v-if="address.isDefault" class="badge">默认</span>
        </h3>
        <p>{{ address.contactPhone }}</p>
        <p>{{ address.cityName }}{{ address.districtName }}{{ address.detail }}</p>
        <div class="actions">
          <button v-if="selecting" @click="emit('selected', address)">使用此地址</button>
          <button @click="edit(address)">编辑</button>
          <button
            v-if="!address.isDefault"
            :disabled="action.busy.value"
            @click="makeDefault(address)"
          >
            设为默认
          </button>
          <button :disabled="action.busy.value" @click="remove(address.id)">删除</button>
        </div>
      </article>
      <p v-if="!loader.busy.value && !addresses.length">还没有地址，请先添加。</p>
    </template>
    <form v-else id="address-editor" @submit.prevent="save">
      <label>
        联系人
        <input name="contactName" v-model.trim="form.contactName" required maxlength="40" />
      </label>
      <label>
        联系电话
        <input
          name="contactPhone"
          v-model.trim="form.contactPhone"
          type="tel"
          required
          pattern="1[0-9]{10}"
          maxlength="11"
        />
      </label>
      <label>
        服务区域
        <select name="districtCode" v-model="form.districtCode" required>
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
        <input name="detail" v-model.trim="form.detail" required maxlength="200" />
      </label>
      <label class="inline">
        <input name="isDefault" v-model="form.isDefault" type="checkbox" />
        设为默认地址
      </label>
      <p class="muted">当前按广州市服务范围预约，无需填写坐标。</p>
    </form>
    <Feedback :error="action.error.value" />
    <template #actions>
      <template v-if="editing">
        <button type="button" :disabled="action.busy.value" @click="backToList">
          返回地址列表
        </button>
        <button form="address-editor" :disabled="action.busy.value || loader.busy.value">
          保存地址
        </button>
      </template>
      <button v-else @click="edit()">新增地址</button>
    </template>
  </ModalPanel>
</template>
