<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { request } from '../api/client'
import type { Schema } from '../api/types'
import { useTask } from '../composables/useTask'
import Feedback from '../components/CustomerFeedback.vue'
const rules = ref<Schema['BookingRulesVO']>(),
  { busy, error, run } = useTask()
function load() {
  return run(async () => {
    rules.value = await request('getBookingRules')
  })
}
onMounted(load)
</script>
<template>
  <h2 v-if="rules">当前服务范围：{{ rules.cityName }}</h2>
  <Feedback :busy="busy" :error="error" />
  <button v-if="error" @click="load">重试</button>
  <section v-if="rules" class="booking-section">
    <h3>预约时间</h3>
    <p>
      最少提前{{ rules.earliestHours }}小时，最远{{ rules.latestDays }}天；每天{{
        rules.workStart
      }}—{{ rules.workEnd }}，按{{
        rules.slotMinutes
      }}分钟起约。服务结束后还需预留缓冲，优惠预约至少提前{{ rules.offerLeadHours }}小时。
    </p>
  </section>
  <section v-if="rules" class="booking-section">
    <h3>服务边界</h3>
    <p>服务范围、配件要求和排除内容以所选服务详情为准；不支持现场议价或接单后加价。</p>
  </section>
  <section v-if="rules" class="booking-section">
    <h3>支付与安排</h3>
    <p>
      创建后{{ rules.paymentTimeoutMinutes }}分钟内模拟支付。标准预约由系统安排，{{
        rules.dispatchWaitMinutes
      }}分钟内未安排成功则取消并全额模拟退款；优惠预约等待人员自主接单，无人接单到期退款。
    </p>
  </section>
  <section v-if="rules" class="booking-section">
    <h3>取消与完成</h3>
    <p>
      人员出发前可取消。出发后不能直接取消；到达后向人员提供开始码。提交完成后请确认服务，{{
        rules.autoConfirmHours
      }}小时后自动完成。仅已完成订单可评价，每单一次。
    </p>
  </section>
</template>
