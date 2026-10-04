<script setup lang="ts">
import type { Schema } from '../api/types'
import { displayTime } from '../utils/format'
import { label } from '../utils/labels'
defineProps<{ entries: Schema['OrderStatusHistoryVO'][] }>()
</script>
<template>
  <section class="booking-section">
    <h3>订单状态记录</h3>
    <ul class="record-list">
      <li v-for="entry in entries" :key="entry.id">
        {{ displayTime(entry.createdAt) }} ·
        {{ entry.fromStatus ? label(entry.fromStatus) + ' → ' : '' }}{{ label(entry.toStatus) }}
        <span>
          ·
          {{
            entry.actorType === 'SYSTEM'
              ? '系统'
              : entry.actorRole
                ? label(entry.actorRole)
                : '用户操作'
          }}
        </span>
        <p v-if="entry.reason">{{ entry.reason }}</p>
      </li>
    </ul>
    <p v-if="!entries.length" class="muted">暂无状态记录</p>
  </section>
</template>
