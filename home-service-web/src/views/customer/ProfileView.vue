<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { request } from '../../api/client'
import { saveSession, sessions } from '../../stores/session'
import { useTask } from '../../composables/useTask'
import Feedback from '../../components/CustomerFeedback.vue'
const form = reactive({ displayName: '', phone: '' }),
  { busy, error, success, run } = useTask()
function load() {
  return run(async () => Object.assign(form, await request('customerCurrentAccount')))
}
function save() {
  return run(
    async (key) => {
      const account = await request('updateCustomerProfile', {
        body: { displayName: form.displayName, phone: form.phone },
        idempotencyKey: key,
      })
      saveSession('customer', { ...sessions.customer!, account })
    },
    '个人资料已保存，地址簿和历史订单保持原联系人',
    JSON.stringify(form),
  )
}
onMounted(load)
</script>
<template>
  <h2>个人信息</h2>
  <p>登录账号：{{ sessions.customer?.account.username }}（不可修改）</p>
  <Feedback :busy="busy" :error="error" :success="success" />
  <form id="profile-form" @submit.prevent="save">
    <label>
      称呼
      <input v-model.trim="form.displayName" required maxlength="40" />
    </label>
    <label>
      个人联系电话
      <input v-model.trim="form.phone" type="tel" required pattern="1[0-9]{10}" maxlength="11" />
    </label>
    <p class="muted">
      个人联系电话用于您的资料。地址簿联系人和每次预约联系人可以不同，不会自动覆盖。
    </p>
  </form>
  <Teleport to="#customer-actions" defer>
    <button class="primary wide-button" form="profile-form" :disabled="busy">保存个人信息</button>
  </Teleport>
</template>
