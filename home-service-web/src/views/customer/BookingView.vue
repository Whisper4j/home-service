<script setup lang="ts">
import { computed, onMounted, onUnmounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createIdempotencyKey, request, useMock } from '../../api/client'
import type { Schema } from '../../api/types'
import { sessions } from '../../stores/session'
import { useTask } from '../../composables/useTask'
import { dateOf, DAY, iso, money } from '../../utils/format'
import { offerReason, suggestedPrice, timeReason } from '../../utils/booking'
import Feedback from '../../components/CustomerFeedback.vue'
import QuoteEditor from '../../components/QuoteEditor.vue'
import SceneImageUpload from '../../components/SceneImageUpload.vue'
const route = useRoute(),
  router = useRouter(),
  loading = useTask(),
  submitting = useTask()
const sku = ref<Schema['SkuVO']>(),
  rules = ref<Schema['BookingRulesVO']>(),
  addresses = ref<Schema['AddressVO'][]>([])
const quoteEditor = ref<InstanceType<typeof QuoteEditor>>()
const now = ref(Date.now()),
  pendingImages = ref(false),
  initialized = ref(false)
const key = `home-service.booking.${sessions.customer?.account.id}.${route.params.skuId}.${route.query.mode || 'STANDARD'}`
const form = reactive({
  addressId: '',
  contactName: '',
  contactPhone: '',
  date: '',
  time: '',
  bookingType: (route.query.mode === 'OFFER' ? 'OFFER' : 'STANDARD') as Schema['BookingType'],
  offerPrice: '',
  remark: '',
  sceneImages: [] as Schema['SceneImageVO'][],
})
let submission = { signature: '', key: '' },
  timer: ReturnType<typeof setInterval> | undefined
try {
  const saved = JSON.parse(sessionStorage.getItem(key) || 'null')
  if (saved) {
    Object.assign(form, saved.form)
    submission = saved.submission
    initialized.value = true
  }
} catch {
  sessionStorage.removeItem(key)
}
function saveDraft() {
  sessionStorage.setItem(key, JSON.stringify({ form, submission }))
}
watch(form, saveDraft, { deep: true, flush: 'sync' })
const address = computed(() => addresses.value.find((a) => a.id === form.addressId))
const start = computed(() => Date.parse(`${form.date}T${form.time}:00+08:00`))
const dates = computed(() =>
  Array.from({ length: (rules.value?.latestDays || 7) + 1 }, (_, i) => dateOf(now.value + i * DAY)),
)
const times = computed(() =>
  Array.from({ length: 28 }, (_, i) => {
    const time = `${String(8 + Math.floor(i / 2)).padStart(2, '0')}:${i % 2 ? '30' : '00'}`
    const stamp = Date.parse(`${form.date}T${time}:00+08:00`)
    return {
      time,
      reason:
        sku.value && rules.value
          ? timeReason(stamp, now.value, sku.value.durationMinutes, form.bookingType, rules.value)
          : '加载中',
    }
  }),
)
const timeError = computed(() =>
  sku.value && rules.value
    ? timeReason(start.value, now.value, sku.value.durationMinutes, form.bookingType, rules.value)
    : '',
)
const offerError = computed(() =>
  form.bookingType === 'OFFER' && sku.value && rules.value
    ? offerReason(start.value, now.value, sku.value, rules.value)
    : '',
)
const amount = computed(() =>
  form.bookingType === 'OFFER' ? form.offerPrice : sku.value?.standardPrice || '0.00',
)
const endTime = computed(() =>
  Number.isFinite(start.value) && sku.value
    ? iso(start.value + sku.value.durationMinutes * 60_000).slice(11, 16)
    : '—',
)
const blocked = computed(
  () =>
    timeError.value ||
    offerError.value ||
    (!address.value ? '请先添加或选择服务地址' : '') ||
    (pendingImages.value ? '请完成上传或移除失败图片' : ''),
)
async function updateNow() {
  now.value = useMock ? (await import('../../mock/transport')).mockNow() : Date.now()
}
function selectAddress() {
  const selected = address.value
  if (selected) {
    form.contactName = selected.contactName
    form.contactPhone = selected.contactPhone
  }
}
async function load() {
  await loading.run(async () => {
    await updateNow()
    const [service, list, config] = await Promise.all([
      request('getCustomerSku', { id: String(route.params.skuId) }),
      request('listAddresses'),
      request('getBookingRules'),
    ])
    if (route.query.entry) {
      const binding = (await request('listClientEntries')).find((e) => e.code === route.query.entry)
      if (!binding?.available || binding.sku?.id !== service.id)
        throw Error('原先选择的服务暂不可预约，请返回重新选择')
    }
    sku.value = service
    addresses.value = list
    rules.value = config
    if (!initialized.value) {
      form.offerPrice = suggestedPrice(service)
      form.date = dateOf(now.value + DAY)
      let candidate = times.value.find(
        (t) =>
          !t.reason &&
          !offerReason(Date.parse(`${form.date}T${t.time}:00+08:00`), now.value, service, config),
      )
      if (form.bookingType === 'OFFER' && !candidate) {
        form.date = dateOf(now.value + 2 * DAY)
        candidate = times.value.find((t) => !t.reason)
      }
      form.time =
        (form.bookingType === 'OFFER' ? candidate : times.value.find((t) => !t.reason))?.time || ''
      initialized.value = true
    }
    const returned = list.find((a) => a.id === route.query.addressId)
    if (returned && returned.id !== form.addressId) {
      form.addressId = returned.id
      selectAddress()
    } else if (!list.some((a) => a.id === form.addressId)) {
      form.addressId = list.find((a) => a.isDefault)?.id || list[0]?.id || ''
      selectAddress()
    }
    saveDraft()
  })
}
function addAddress() {
  saveDraft()
  void router.push({ path: '/customer/addresses', query: { returnTo: route.fullPath, add: '1' } })
}
function changeDate() {
  form.time = ''
}
function switchStandard() {
  form.bookingType = 'STANDARD'
}
function setPending(value: boolean) {
  pendingImages.value = value
}
async function submit() {
  await updateNow()
  if (form.bookingType === 'OFFER' && !quoteEditor.value?.validate()) return
  if (blocked.value || !sku.value) return
  const body: Schema['CreateOrderDTO'] = {
    skuId: sku.value.id,
    addressId: form.addressId,
    contactName: form.contactName.trim(),
    contactPhone: form.contactPhone.trim(),
    bookingType: form.bookingType,
    startTime: iso(start.value),
    remark: form.remark.trim(),
    sceneImageIds: form.sceneImages.map((i) => i.id),
    ...(form.bookingType === 'OFFER'
      ? { offerPrice: money(Math.round(Number(form.offerPrice) * 100)) }
      : {}),
  }
  const signature = JSON.stringify(body)
  if (submission.signature !== signature || !submission.key)
    submission = { signature, key: createIdempotencyKey() }
  saveDraft()
  await submitting.run(
    async () => {
      const order = await request('createOrder', { body, idempotencyKey: submission.key })
      sessionStorage.removeItem(key)
      await router.replace(`/customer/pay/${order.id}`)
    },
    '',
    signature,
  )
}
onMounted(() => {
  void load()
  timer = setInterval(() => void updateNow(), 30_000)
})
onUnmounted(() => clearInterval(timer))
</script>
<template>
  <Feedback :busy="loading.busy.value" :error="loading.error.value" />
  <button v-if="loading.error.value" @click="load">重新加载预约</button>
  <form
    v-if="sku && rules"
    id="booking-form"
    class="booking-panel"
    aria-label="预约面板"
    @submit.prevent="submit"
  >
    <section class="booking-section">
      <h2>{{ sku.name }}</h2>
      <p>
        {{ sku.durationMinutes }} 分钟 ·
        {{ form.bookingType === 'OFFER' ? '优惠预约 · 人员自主接单' : '标准预约 · 系统自动安排' }}
      </p>
      <p>标准价 ¥{{ sku.standardPrice }}</p>
    </section>
    <section class="booking-section">
      <h2>服务地址</h2>
      <div class="fields">
        <label v-if="addresses.length">
          选择地址
          <select name="addressId" v-model="form.addressId" @change="selectAddress">
            <option v-for="item in addresses" :key="item.id" :value="item.id">
              {{ item.isDefault ? '默认 · ' : '' }}{{ item.districtName }} {{ item.detail }}
            </option>
          </select>
        </label>
        <p v-if="address">
          {{ address.contactName }} · {{ address.contactPhone }}
          <br />
          {{ address.cityName }}{{ address.districtName }}{{ address.detail }}
        </p>
        <p v-else>还没有服务地址，请先添加。</p>
        <button type="button" @click="addAddress">新增地址并返回预约</button>
      </div>
    </section>
    <section class="booking-section">
      <h2>本次联系人</h2>
      <div class="fields">
        <label>
          本次联系人
          <input
            name="contactName"
            v-model.trim="form.contactName"
            required
            maxlength="40"
            autocomplete="name"
          />
        </label>
        <label>
          本次联系电话
          <input
            name="contactPhone"
            v-model.trim="form.contactPhone"
            type="tel"
            required
            pattern="1[0-9]{10}"
            maxlength="11"
          />
        </label>
        <p class="muted">可替家人预约。这里只修改本次订单，不覆盖个人资料或地址簿。</p>
      </div>
    </section>
    <section class="booking-section">
      <h2>日期与时间</h2>
      <div class="fields">
        <label>
          预约日期
          <select name="date" v-model="form.date" required @change="changeDate">
            <option v-for="date in dates" :key="date" :value="date">{{ date }}</option>
          </select>
        </label>
        <label>
          开始时间
          <select name="time" v-model="form.time" required>
            <option value="" disabled>请选择半小时粒度的开始时间</option>
            <option
              v-for="slot in times"
              :key="slot.time"
              :value="slot.time"
              :disabled="Boolean(slot.reason)"
            >
              {{ slot.time }}{{ slot.reason ? ' · ' + slot.reason : '' }}
            </option>
          </select>
        </label>
        <p>预计服务结束：{{ endTime }}（北京时间）</p>
        <p class="muted">
          至少提前{{ rules.earliestHours }}小时，最远{{ rules.latestDays }}天。服务及{{
            form.bookingType === 'STANDARD'
              ? rules.standardBufferMinutes
              : rules.offerBufferMinutes
          }}分钟缓冲须在{{ rules.workStart }}—{{
            rules.workEnd
          }}内。候选时间不代表已有人员，最终以支付后的安排或接单结果为准。
        </p>
        <p v-if="timeError" role="status">{{ timeError }}</p>
        <p v-if="offerError" role="alert">{{ offerError }}</p>
        <button v-if="offerError" type="button" @click="switchStandard">我选择改为标准预约</button>
      </div>
    </section>
    <section v-if="form.bookingType === 'OFFER'" class="booking-section">
      <h2>设置优惠报价</h2>
      <QuoteEditor ref="quoteEditor" v-model="form.offerPrice" :range="sku" />
      <p class="muted">需低于标准价。不承诺有人接单，不会自动加价或转标准预约。</p>
    </section>
    <section class="booking-section">
      <label>
        额外要求（可选）
        <textarea
          name="remark"
          v-model="form.remark"
          maxlength="300"
          placeholder="例如重点清洁厨房、家中有宠物、请提前联系"
        />
      </label>
    </section>
    <section class="booking-section">
      <h2>现场图片</h2>
      <p v-if="form.bookingType === 'OFFER'">帮助符合资格的人员在接单前了解现场情况。</p>
      <SceneImageUpload v-model="form.sceneImages" @pending="setPending" />
    </section>
    <Feedback :error="submitting.error.value" />
    <Teleport to="#customer-actions" defer>
      <p v-if="blocked" class="muted">{{ blocked }}</p>
      <div class="amount-bar">
        <strong>¥{{ amount }}</strong>
        <button
          class="primary"
          form="booking-form"
          type="submit"
          :disabled="Boolean(blocked) || submitting.busy.value || loading.busy.value"
        >
          {{ submitting.busy.value ? '正在提交…' : '提交预约并去支付' }}
        </button>
      </div>
    </Teleport>
  </form>
</template>
