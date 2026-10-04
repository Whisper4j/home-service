<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { request } from '../api/client'
import type { Schema } from '../api/types'
import { useTask } from '../composables/useTask'
import Feedback from './Feedback.vue'
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
  <Feedback :busy="busy" :error="error" />
  <button v-if="error" @click="load">重试</button>
  <section v-if="rules" class="booking-section">
    <h2>接单与履约</h2>
    <p>
      标准订单由系统直接分配，无需接受或拒绝。优惠订单由符合技能、城市、工作时间及完整服务和缓冲条件的人员自主接单。
    </p>
    <p>
      按出发、到达、向客户索取开始码、核验后开始、提交完成的顺序操作。提交完成后等待客户确认，{{
        rules.autoConfirmHours
      }}小时未确认自动完成。
    </p>
    <p>
      接单后锁定成交金额，不能自行取消、修改价格或现场议价。现场图片仅供了解约定服务，不扩大套餐范围。
    </p>
  </section>
  <section v-if="rules" class="booking-section">
    <h2>工作时间与请假</h2>
    <p>
      所有非休息日沿用统一工作区间。标准单服务后预留{{
        rules.standardBufferMinutes
      }}分钟，优惠单预留{{ rules.offerBufferMinutes }}分钟；这些时间不可再次接单。
    </p>
    <p>
      请假至少提前{{ rules.leaveLeadHours }}小时，以{{
        rules.slotMinutes
      }}分钟对齐，不得占用已接订单或缓冲时间。调度资格由平台维护。
    </p>
    <p>
      抢单池按支付成功首次入池时间从早到晚，同时间按数值编号排序，调价不改变位置，是本项目规则。通知仅在网页在线时提供，操作最终以HTTP返回结果为准。
    </p>
  </section>
</template>
