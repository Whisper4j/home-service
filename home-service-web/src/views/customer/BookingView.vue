<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { request, useMock } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { fromLocalInput, HOUR, localInput, tomorrowMorning } from '../../utils/format'
import Feedback from '../../components/Feedback.vue'
const route = useRoute(),
  router = useRouter()
const sku = ref<Schema['SkuVO']>(),
  addresses = ref<Schema['AddressVO'][]>([]),
  rules = ref<Schema['BookingRulesVO']>()
const now = ref(Date.now()),
  initialized = ref(false)
const form = reactive({
  addressId: '',
  bookingType: 'STANDARD' as Schema['BookingType'],
  startTime: '',
  offerPrice: '',
  remark: '',
})
const { busy, error, run } = useTask()
const offerAllowed = computed(
  () =>
    sku.value?.supportsOffer && Date.parse(fromLocalInput(form.startTime)) >= now.value + 12 * HOUR,
)
async function load() {
  await run(async () => {
    if (useMock) now.value = (await import('../../mock/transport')).mockNow()
    const [service, addressList, config] = await Promise.all([
      request('getCustomerSku', { id: String(route.params.skuId) }),
      request('listAddresses'),
      request('getBookingRules'),
    ])
    sku.value = service
    addresses.value = addressList
    rules.value = config
    if (!initialized.value) {
      form.startTime = localInput(tomorrowMorning(now.value))
      form.offerPrice = service.minimumOfferPrice
      form.addressId = addressList.find((a) => a.isDefault)?.id || addressList[0]?.id || ''
      initialized.value = true
    }
  })
}
async function submit() {
  await run(
    async (key) => {
      const order = await request('createOrder', {
        body: {
          skuId: sku.value!.id,
          addressId: form.addressId,
          bookingType: form.bookingType,
          startTime: fromLocalInput(form.startTime),
          ...(form.bookingType === 'OFFER' ? { offerPrice: form.offerPrice } : {}),
          remark: form.remark,
        },
        idempotencyKey: key,
      })
      await router.push(`/customer/orders/${order.id}`)
    },
    '',
    JSON.stringify(form),
  )
}
useRefresh(load)
</script>
<template>
  <h1>创建预约</h1>
  <Feedback :error="error" :busy="busy" />
  <div v-if="sku && rules" class="grid">
    <section class="panel">
      <h2>{{ sku.name }}</h2>
      <dl>
        <dt>标准价格</dt>
        <dd>¥{{ sku.standardPrice }}</dd>
        <dt>服务时长</dt>
        <dd>{{ sku.durationMinutes }} 分钟</dd>
        <dt>包含内容</dt>
        <dd>{{ sku.included }}</dd>
        <dt>排除内容</dt>
        <dd>{{ sku.excluded }}</dd>
        <dt>自备配件</dt>
        <dd>{{ sku.customerSuppliesParts ? '需要' : '不需要' }}</dd>
      </dl>
      <p>
        最早提前 {{ rules.earliestHours }} 小时，最远
        {{ rules.latestDays }} 天；半小时起约。服务及尾部缓冲须在 08:00—22:00 内。
      </p>
      <p>标准预约尾部缓冲120分钟，优惠预约60分钟。支付后才安排人员，暂不保证一定派单成功。</p>
    </section>
    <section class="panel">
      <form @submit.prevent="submit">
        <label>
          服务地址
          <select v-model="form.addressId" required>
            <option value="" disabled>请选择地址</option>
            <option v-for="address in addresses" :key="address.id" :value="address.id">
              {{ address.districtName }} {{ address.detail }} · {{ address.contactName }}
            </option>
          </select>
        </label>
        <RouterLink to="/customer/addresses">维护地址</RouterLink>
        <label>
          预约开始（北京时间）
          <input
            v-model="form.startTime"
            type="datetime-local"
            step="1800"
            required
            @change="!offerAllowed && (form.bookingType = 'STANDARD')"
          />
        </label>
        <label>
          预约方式
          <select v-model="form.bookingType">
            <option value="STANDARD">标准预约 · 系统自动派单</option>
            <option value="OFFER" :disabled="!offerAllowed">优惠预约 · 人员自主抢单</option>
          </select>
        </label>
        <p v-if="!offerAllowed" class="muted">
          仅支持优惠的清洁规格且提前至少12小时可选择优惠预约。
        </p>
        <label v-if="form.bookingType === 'OFFER'">
          报价（最低 {{ sku.minimumOfferPrice }}，低于 {{ sku.standardPrice }}，步长5元）
          <input
            v-model="form.offerPrice"
            inputmode="decimal"
            pattern="[0-9]+\.[0-9]{2}"
            required
          />
        </label>
        <label>
          备注
          <textarea v-model.trim="form.remark" maxlength="300" />
        </label>
        <button :disabled="busy || !addresses.length">创建预约，前往模拟支付</button>
      </form>
    </section>
  </div>
</template>
