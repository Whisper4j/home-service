<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import type { Schema } from '../api/types'
import { request } from '../api/client'
import { useTask } from '../composables/useTask'
import { quoteReason } from '../utils/quote'
import { money } from '../utils/format'
import QuoteEditor from './QuoteEditor.vue'
import Feedback from './Feedback.vue'
const props = defineProps<{ order: Schema['OrderVO'] }>()
const emit = defineEmits<{ close: []; saved: [] }>()
const dialog = ref<HTMLDialogElement>(),
  editor = ref<InstanceType<typeof QuoteEditor>>()
const snapshot = ref(JSON.parse(JSON.stringify(props.order)) as Schema['OrderVO']),
  price = ref(props.order.currentPrice),
  confirmed = ref(false),
  latest = ref<Schema['OrderVO']>()
const rules = ref<Schema['BookingRulesVO']>()
const task = useTask(),
  latestTask = useTask()
const conflict = computed(
  () =>
    task.code.value === 'PRICE_CHANGED' ||
    (latest.value && latest.value.priceVersion !== snapshot.value.priceVersion),
)
const higher = computed(() => Number(price.value) > Number(snapshot.value.currentPrice))
const unchanged = computed(() => Number(price.value) === Number(snapshot.value.currentPrice))
watch(
  () => props.order,
  (value) => {
    latest.value = JSON.parse(JSON.stringify(value))
  },
  { deep: true },
)
function close() {
  if (!task.busy.value) emit('close')
}
async function checkLatest() {
  await latestTask.run(async () => {
    latest.value = await request('customerGetOrder', { id: snapshot.value.id })
  })
}
function acceptLatest() {
  if (!latest.value) return
  snapshot.value = JSON.parse(JSON.stringify(latest.value))
  price.value = latest.value.currentPrice
  confirmed.value = false
  task.error.value = ''
  task.code.value = ''
  latest.value = undefined
}
async function submit() {
  if (
    !rules.value ||
    !snapshot.value.allowedActions.includes('CHANGE_OFFER') ||
    (latest.value && !latest.value.allowedActions.includes('CHANGE_OFFER')) ||
    !editor.value?.validate() ||
    conflict.value ||
    unchanged.value ||
    (higher.value && !confirmed.value)
  )
    return
  const body = {
    expectedPrice: snapshot.value.currentPrice,
    priceVersion: snapshot.value.priceVersion,
    newPrice: money(Math.round(Number(price.value) * 100)),
    confirmSimulatedPayment: confirmed.value,
  }
  await task.run(
    async (key) => {
      await request('changeOffer', { id: snapshot.value.id, body, idempotencyKey: key })
      emit('saved')
      emit('close')
    },
    '',
    JSON.stringify(body),
  )
}
function loadRules() {
  return latestTask.run(async () => {
    rules.value = await request('getBookingRules')
  })
}
onMounted(() => {
  dialog.value?.showModal()
  void loadRules()
})
</script>
<template>
  <dialog
    ref="dialog"
    class="customer-dialog flow-dialog"
    aria-label="调整优惠报价"
    @cancel.prevent="close"
  >
    <header>
      <h2>调整优惠报价</h2>
      <button :disabled="task.busy.value" @click="close">关闭</button>
    </header>
    <form id="quote-form" class="dialog-body" @submit.prevent="submit">
      <QuoteEditor
        v-if="rules"
        ref="editor"
        v-model="price"
        :range="snapshot.service"
        :price-step="rules.priceStep"
        :current="snapshot.currentPrice"
      />
      <label v-if="higher" class="inline">
        <input v-model="confirmed" type="checkbox" />
        我确认模拟补付上述差额
      </label>
      <p v-else-if="!unchanged">确认降价后，将退回差额并保留退款记录。</p>
      <Feedback :error="task.error.value" />
      <Feedback :error="latestTask.error.value" />
      <button v-if="!rules && latestTask.error.value" type="button" @click="loadRules">
        重新加载报价规则
      </button>
      <section v-if="conflict" class="panel" role="alert">
        <p>
          原确认报价 ¥{{ snapshot.currentPrice }}；最新报价 ¥{{
            latest?.currentPrice || task.details.value.currentPrice
          }}。请先查看最新报价，再重新填写并确认。
        </p>
        <button type="button" :disabled="task.busy.value" @click="checkLatest">查看最新报价</button>
        <button v-if="latest" type="button" @click="acceptLatest">
          我已查看，按最新报价重新填写
        </button>
      </section>
      <p
        v-if="
          !snapshot.allowedActions.includes('CHANGE_OFFER') ||
          (latest && !latest.allowedActions.includes('CHANGE_OFFER'))
        "
      >
        当前订单已不可调价，请关闭并刷新订单。
      </p>
    </form>
    <footer>
      <button
        form="quote-form"
        :disabled="
          task.busy.value ||
          Boolean(conflict) ||
          unchanged ||
          Boolean(!rules || quoteReason(price, snapshot.service, rules.priceStep)) ||
          (higher && !confirmed) ||
          !snapshot.allowedActions.includes('CHANGE_OFFER') ||
          (latest && !latest.allowedActions.includes('CHANGE_OFFER'))
        "
      >
        确认报价调整
      </button>
    </footer>
  </dialog>
</template>
