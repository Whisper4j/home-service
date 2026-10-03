<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { quoteBounds } from '../../utils/quote'
import { money } from '../../utils/format'
import { clientEntries } from '../../utils/clientEntries'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/CustomerFeedback.vue'
const route = useRoute(),
  router = useRouter(),
  { busy, error, run } = useTask()
const entry = ref<Schema['ClientEntryVO']>()
const info = computed(() => clientEntries.find((e) => e.code === route.params.code))
const sku = computed(() => entry.value?.sku)
function load() {
  return run(async () => {
    entry.value = (await request('listClientEntries')).find((e) => e.code === route.params.code)
  })
}
function book(mode: Schema['BookingType']) {
  if (sku.value)
    void router.push({
      path: `/customer/booking/${sku.value.id}`,
      query: { mode, entry: entry.value!.code },
    })
}
useRefresh(load)
</script>
<template>
  <Feedback :busy="busy" :error="error" />
  <button v-if="error" @click="load">重新加载</button>
  <template v-if="sku && entry?.available">
    <h2>{{ sku.name }}</h2>
    <p>
      <strong>标准价 ¥{{ sku.standardPrice }}</strong>
      · {{ sku.durationMinutes }} 分钟
    </p>
    <section class="booking-section">
      <h3>适合什么情况</h3>
      <p>{{ info?.hint }}</p>
      <p>{{ sku.description }}</p>
    </section>
    <section class="booking-section">
      <h3>包含内容</h3>
      <p>{{ sku.included }}</p>
      <h3>不包含</h3>
      <p>{{ sku.excluded }}</p>
      <p>
        配件：{{
          sku.customerSuppliesParts ? '请客户自备适配配件，价格不含配件费用' : '无需客户自备配件'
        }}
      </p>
    </section>
    <section class="booking-section">
      <h3>预约提醒</h3>
      <p v-if="info?.group === 'daily'">
        按预约时长提供服务，面积仅供参考，实际完成范围与现场整洁程度和清洁内容有关。
      </p>
      <p v-if="info?.group === 'deep'">深度清洁不包含装修后的开荒保洁。</p>
      <p>请提前确认服务范围。支付后安排人员，不支持现场议价或接单后加价。</p>
    </section>
    <section class="booking-section">
      <p v-if="sku.supportsOffer">优惠预约：您设置报价，符合条件的服务人员自主接单，不保证接单。</p>
      <p>标准预约：按标准价格预约，由系统自动安排人员。</p>
    </section>
    <Teleport to="#customer-actions" defer>
      <div :class="sku.supportsOffer ? 'action-pair' : ''">
        <button
          :disabled="quoteBounds(sku).high < quoteBounds(sku).low"
          v-if="sku.supportsOffer"
          @click="book('OFFER')"
        >
          <strong>优惠预约</strong>
          <small>
            {{
              quoteBounds(sku).high < quoteBounds(sku).low
                ? '暂无合法优惠报价'
                : `最低 ¥${money(quoteBounds(sku).low)}`
            }}
          </small>
        </button>
        <button class="primary wide-button" @click="book('STANDARD')">
          <strong>标准预约</strong>
          <small>¥{{ sku.standardPrice }}</small>
        </button>
      </div>
    </Teleport>
  </template>
  <p v-else-if="!busy && !error" class="empty">该服务暂不可预约，请返回选择其他服务。</p>
</template>
