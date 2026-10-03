<script setup lang="ts">
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { displayTime } from '../../utils/format'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
import SceneImages from '../../components/SceneImages.vue'
const id = String(useRoute().params.id),
  order = ref<Schema['OrderVO']>(),
  enteredCode = ref(''),
  loader = useTask(),
  action = useTask()
function load() {
  return loader.run(async () => {
    order.value = await request('workerGetOrder', { id })
  })
}
function act(operation: 'departOrder' | 'arriveOrder' | 'startOrder' | 'finishOrder') {
  return action.run(
    async (key) => {
      order.value =
        operation === 'startOrder'
          ? await request(operation, {
              id,
              body: { startCode: enteredCode.value },
              idempotencyKey: key,
            })
          : await request(operation, { id, idempotencyKey: key })
      enteredCode.value = ''
    },
    '履约进度已更新',
    `${operation}:${id}:${enteredCode.value}`,
  )
}
useRefresh(load)
</script>
<template>
  <Feedback :busy="loader.busy.value" :error="loader.error.value" />
  <button @click="load">刷新任务</button>
  <template v-if="order">
    <section class="booking-section">
      <h2>{{ order.service.skuName }}</h2>
      <p>
        <strong>{{ label(order.status) }}</strong>
        · {{ order.bookingType === 'STANDARD' ? '系统分配' : '自主抢单' }}
      </p>
      <p>{{ displayTime(order.startTime) }} — {{ displayTime(order.endTime) }}</p>
      <p>服务后预留至 {{ displayTime(order.bufferEndTime) }}</p>
      <p>锁定订单金额 ¥{{ order.dealPrice }}（非人员收入）</p>
    </section>
    <section class="booking-section">
      <h3>服务地址与本次联系人</h3>
      <p>{{ order.contactName }} · {{ order.contactPhone }}</p>
      <p>{{ order.address.cityName }}{{ order.address.districtName }}{{ order.address.detail }}</p>
      <p>额外要求：{{ order.remark || '无' }}</p>
      <SceneImages :images="order.sceneImages" role="worker" :order-id="id" />
    </section>
    <section class="booking-section">
      <h3>约定服务范围</h3>
      <p>包含：{{ order.service.included }}</p>
      <p>排除：{{ order.service.excluded }}</p>
      <p>客户自备配件：{{ order.service.customerSuppliesParts ? '需要' : '不需要' }}</p>
    </section>
    <Feedback :error="action.error.value" :success="action.success.value" />
    <form v-if="order.status === 'ARRIVED'" id="start-service" @submit.prevent="act('startOrder')">
      <label>
        向客户索取服务开始码
        <input
          name="enteredCode"
          v-model="enteredCode"
          inputmode="numeric"
          pattern="[0-9]{6}"
          maxlength="6"
          required
        />
      </label>
      <p>到达后核验客户提供的开始码，且到预约开始时间才能开始服务。</p>
    </form>
    <p v-if="order.status === 'PENDING_CONFIRMATION'">
      已提交完成，等待客户确认；{{ displayTime(order.confirmationDeadline) }}
      自动完成。此时还不计入已完成服务数据。
    </p>
    <p v-if="order.cancellationReason">取消原因：{{ order.cancellationReason }}</p>
    <p class="muted">
      订单 {{ id }}。按出发、到达、开始、提交完成顺序履约，无法自行取消或修改成交金额。
    </p>
    <Teleport
      v-if="['PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE'].includes(order.status)"
      to="#worker-actions"
      defer
    >
      <button
        v-if="order.status === 'PENDING_SERVICE'"
        class="wide-button"
        :disabled="action.busy.value"
        @click="act('departOrder')"
      >
        确认出发
      </button>
      <button
        v-else-if="order.status === 'DEPARTED'"
        class="wide-button"
        :disabled="action.busy.value"
        @click="act('arriveOrder')"
      >
        确认到达
      </button>
      <button
        v-else-if="order.status === 'ARRIVED'"
        class="wide-button"
        form="start-service"
        :disabled="action.busy.value"
      >
        校验并开始服务
      </button>
      <button v-else class="wide-button" :disabled="action.busy.value" @click="act('finishOrder')">
        提交服务完成
      </button>
    </Teleport>
  </template>
</template>
