<script setup lang="ts">
import type { Schema } from '../api/types'
import { displayTime } from '../utils/format'
import { label } from '../utils/labels'
defineProps<{ order: Schema['OrderVO']; returnTo: string }>()
</script>
<template>
  <article class="panel order-card">
    <div class="card-heading">
      <h2>{{ order.service.skuName }}</h2>
      <span class="badge">{{ label(order.status) }}</span>
    </div>
    <p>{{ displayTime(order.startTime) }} · {{ order.service.durationMinutes }}分钟</p>
    <p>
      {{ order.bookingType === 'STANDARD' ? '系统分配' : '自主抢单' }} · 订单金额 ¥{{
        order.dealPrice || order.currentPrice
      }}
    </p>
    <p>{{ order.address.districtName }} · {{ order.contactName }}</p>
    <RouterLink
      class="button wide-button"
      :to="{ path: `/worker/orders/${order.id}`, query: { returnTo } }"
    >
      查看任务 / 履约
    </RouterLink>
  </article>
</template>
