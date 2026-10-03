<script setup lang="ts">
import { reactive } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
import { request } from '../../api/client'
import { useTask } from '../../composables/useTask'
import Feedback from '../../components/Feedback.vue'
const form = reactive({ username: '', password: '', displayName: '', phone: '' })
const { busy, error, success, run } = useTask()
function submit() {
  void run(
    (key) => request('customerRegister', { body: { ...form }, idempotencyKey: key }),
    '注册成功，请使用新账号登录',
    JSON.stringify(form),
  )
}
</script>
<template>
  <main class="auth">
    <h1>注册客户账号</h1>
    <form @submit.prevent="submit">
      <label>
        用户名
        <input
          name="username"
          v-model.trim="form.username"
          required
          pattern="[A-Za-z][A-Za-z0-9_]{2,31}"
          title="字母开头，3至32位字母、数字或下划线"
        />
      </label>
      <label>
        密码
        <input
          name="password"
          v-model="form.password"
          type="password"
          required
          minlength="8"
          maxlength="72"
          autocomplete="new-password"
        />
      </label>
      <label>
        称呼
        <input name="displayName" v-model.trim="form.displayName" required maxlength="40" />
      </label>
      <label>
        联系电话
        <input name="phone" v-model.trim="form.phone" required pattern="1[0-9]{10}" />
      </label>
      <button :disabled="busy">注册</button>
    </form>
    <Feedback :error="error" :success="success" :busy="busy" />
    <RouterLink :to="{ path: '/customer/login', query: route.query }">返回客户登录</RouterLink>
  </main>
</template>
