<script setup lang="ts">
import { ref } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { sessions, saveSession } from '../../stores/session'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
const profile = ref<Schema['WorkerProfileVO']>(),
  phone = ref(''),
  loader = useTask(),
  action = useTask()
let initialized = false
function load() {
  return loader.run(async () => {
    profile.value = await request('getWorkerProfile')
    if (!initialized) {
      phone.value = profile.value.phone
      initialized = true
    }
  })
}
function save() {
  return action.run(
    async (key) => {
      profile.value = await request('updateWorkerContact', {
        body: { phone: phone.value },
        idempotencyKey: key,
      })
      const session = sessions.worker
      if (session)
        saveSession('worker', {
          ...session,
          account: { ...session.account, phone: profile.value.phone },
        })
    },
    '联系电话已更新',
    phone.value,
  )
}
useRefresh(load)
</script>
<template>
  <Feedback :busy="loader.busy.value" :error="loader.error.value" />
  <button v-if="loader.error.value" @click="load">重新加载</button>
  <template v-if="profile">
    <dl>
      <dt>姓名</dt>
      <dd>{{ profile.displayName }}</dd>
      <dt>账号</dt>
      <dd>{{ profile.username }}</dd>
      <dt>技能</dt>
      <dd>{{ profile.skills.map((skill) => skill.name).join('、') }}</dd>
      <dt>城市</dt>
      <dd>广州市</dd>
      <dt>账号状态</dt>
      <dd>{{ label(profile.status) }}</dd>
      <dt>调度资格</dt>
      <dd>{{ profile.dispatchEnabled ? '允许新分配' : '暂停新分配，已有任务继续履约' }}</dd>
    </dl>
    <p class="muted">以上资料由平台维护。如需变更，请联系平台。</p>
    <form id="worker-contact" @submit.prevent="save">
      <label>
        联系电话
        <input
          name="phone"
          v-model.trim="phone"
          type="tel"
          pattern="1[0-9]{10}"
          maxlength="11"
          required
        />
      </label>
      <p>仅修改本人联系方式，不修改客户或历史订单联系人。</p>
    </form>
    <Feedback :error="action.error.value" :success="action.success.value" />
    <Teleport to="#worker-actions" defer>
      <button
        class="wide-button"
        form="worker-contact"
        :disabled="action.busy.value || phone === profile.phone"
      >
        保存联系电话
      </button>
    </Teleport>
  </template>
</template>
