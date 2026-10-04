<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { quoteBounds, quoteReason, suggestedPrice, type PriceRange } from '../utils/quote'
import { cents, money } from '../utils/format'
import { showError } from '../stores/feedback'
const props = defineProps<{ range: PriceRange; priceStep: string; current?: string }>()
const value = defineModel<string>({ required: true })
const error = ref('')
const bounds = computed(() => quoteBounds(props.range, props.priceStep))
const reason = computed(() => quoteReason(value.value, props.range, props.priceStep))
const difference = computed(() =>
  props.current && !reason.value
    ? Math.round(Number(value.value) * 100) - Math.round(Number(props.current) * 100)
    : 0,
)
function validate() {
  error.value = reason.value
  if (error.value) showError(error.value)
  return !error.value
}
function canStep(delta: number) {
  const next = Math.round(Number(value.value) * 100) + delta
  return (
    Number.isSafeInteger(next) &&
    next >= 0 &&
    !reason.value &&
    !quoteReason(money(Math.round(Number(value.value) * 100) + delta), props.range, props.priceStep)
  )
}
function step(delta: number) {
  if (canStep(delta)) value.value = money(Math.round(Number(value.value) * 100) + delta)
}
watch(value, () => {
  if (error.value && !reason.value) error.value = ''
})
defineExpose({ validate })
</script>
<template>
  <p v-if="current">当前价 ¥{{ current }}</p>
  <p>标准价 ¥{{ range.standardPrice }} · 最低可报价 ¥{{ money(bounds.low) }}</p>
  <p v-if="!current && suggestedPrice(range, priceStep)">
    建议报价 ¥{{ suggestedPrice(range, priceStep) }}：价格中点向下对齐{{
      priceStep
    }}元并限制在合法范围，不代表接单预测。
  </p>
  <div class="quote-controls">
    <button type="button" :disabled="!canStep(-cents(priceStep))" @click="step(-cents(priceStep))">
      －{{ priceStep }}元
    </button>
    <label>
      {{ current ? '新报价' : '当前报价' }}
      <input
        v-model="value"
        data-quote
        inputmode="decimal"
        :aria-invalid="Boolean(error)"
        @blur="validate"
      />
    </label>
    <button type="button" :disabled="!canStep(cents(priceStep))" @click="step(cents(priceStep))">
      ＋{{ priceStep }}元
    </button>
  </div>
  <p v-if="error" class="field-error" role="status">{{ error }}</p>
  <p class="muted">报价必须为 {{ priceStep }} 元整数倍，不低于最低价且低于标准价。</p>
  <p v-if="current">
    {{
      difference > 0
        ? `需模拟补付 ¥${money(difference)}`
        : difference < 0
          ? `确认后模拟退款 ¥${money(-difference)}`
          : '报价未变化，无需提交'
    }}
  </p>
</template>
