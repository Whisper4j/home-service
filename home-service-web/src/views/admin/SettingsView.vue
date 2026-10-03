<script setup lang="ts">
import { reactive, ref } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
const rules = ref<Schema['BookingRulesVO']>(),
  form = reactive<Schema['SettingsDTO']>({ earliestHours: 2, latestDays: 7 })
const { busy, error, success, run } = useTask()
function load() {
  return run(async () => {
    rules.value = await request('getSettings')
    form.earliestHours = rules.value.earliestHours
    form.latestDays = rules.value.latestDays
  })
}
function save() {
  void run(
    async (key) => {
      rules.value = await request('updateSettings', { body: { ...form }, idempotencyKey: key })
    },
    '配置已保存，仅影响新的预约',
    JSON.stringify(form),
  )
}
useRefresh(load)
</script>
<template>
  <h1>平台配置</h1>
  <Feedback :error="error" :success="success" :busy="busy" />
  <section class="panel">
    <h2>预约窗口</h2>
    <form @submit.prevent="save">
      <div class="form-grid">
        <label>
          最早提前小时数
          <input
            name="earliestHours"
            v-model.number="form.earliestHours"
            type="number"
            min="2"
            max="24"
            required
          />
        </label>
        <label>
          最远预约天数
          <input
            name="latestDays"
            v-model.number="form.latestDays"
            type="number"
            min="1"
            max="7"
            required
          />
        </label>
      </div>
      <button :disabled="busy">保存预约窗口</button>
    </form>
  </section>
  <section v-if="rules" class="panel">
    <h2>当前固定业务规则</h2>
    <dl>
      <dt>业务时区</dt>
      <dd>Asia/Shanghai（北京时间）</dd>
      <dt>工作时间</dt>
      <dd>{{ rules.workStart }}—{{ rules.workEnd }}</dd>
      <dt>时间槽</dt>
      <dd>{{ rules.slotMinutes }} 分钟</dd>
      <dt>优惠提前量</dt>
      <dd>{{ rules.offerLeadHours }} 小时</dd>
      <dt>报价步长</dt>
      <dd>¥{{ rules.priceStep }}</dd>
      <dt>支付超时</dt>
      <dd>{{ rules.paymentTimeoutMinutes }} 分钟</dd>
      <dt>标准调度</dt>
      <dd>每 {{ rules.dispatchScanSeconds }} 秒扫描，最长 {{ rules.dispatchWaitMinutes }} 分钟</dd>
      <dt>尾部缓冲</dt>
      <dd>
        标准 {{ rules.standardBufferMinutes }} 分钟 / 优惠 {{ rules.offerBufferMinutes }} 分钟
      </dd>
      <dt>优惠截止</dt>
      <dd>
        支付后 {{ rules.offerWaitMinutes }} 分钟与开始前
        {{ rules.offerSafetyHours }} 小时中的较早时间
      </dd>
      <dt>自动确认</dt>
      <dd>提交完成后 {{ rules.autoConfirmHours }} 小时</dd>
    </dl>
  </section>
</template>
