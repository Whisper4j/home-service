<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import type { Schema } from '../api/types'
import { quoteBounds, quoteReason, suggestedPrice, type PriceRange } from '../utils/quote'
import { money } from '../utils/format'
import { showError } from '../stores/feedback'
const props = withDefaults(
  defineProps<{ range: PriceRange; rule?: Schema['OfferPriceRule']; current?: string }>(),
  { rule: 'MULTIPLE_OF_FIVE' },
)
const value = defineModel<string>({ required: true })
const error = ref('')
const bounds = computed(() => quoteBounds(props.range, props.rule))
const reason = computed(() => quoteReason(value.value, props.range, props.rule))
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
  return (
    !reason.value &&
    !quoteReason(money(Math.round(Number(value.value) * 100) + delta), props.range, props.rule)
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
  <p v-if="!current && suggestedPrice(range)">
    建议报价 ¥{{ suggestedPrice(range) }}：价格中点向下对齐5元并限制在合法范围，不代表接单预测。
  </p>
  <div class="quote-controls">
    <button type="button" :disabled="!canStep(-500)" @click="step(-500)">－5元</button>
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
    <button type="button" :disabled="!canStep(500)" @click="step(500)">＋5元</button>
  </div>
  <p v-if="error" class="field-error" role="status">{{ error }}</p>
  <p class="muted">
    {{
      rule === 'MINIMUM_ANCHORED'
        ? '此历史订单按原最低价起每5元调整。'
        : '报价必须为5元整数倍，不低于最低价且低于标准价。'
    }}
  </p>
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
