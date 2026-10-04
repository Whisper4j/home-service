<script setup lang="ts">
import { reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { RolePath, Schema } from '../../api/types'
import { request } from '../../api/client'
import { saveSession } from '../../stores/session'
import { roleLabels } from '../../utils/labels'
import { useTask } from '../../composables/useTask'
import Feedback from '../../components/Feedback.vue'
const props = defineProps<{ role: RolePath }>()
const form = reactive<Schema['LoginDTO']>({
  username: '',
  password: '',
})
watch(
  () => props.role,
  () => {
    form.username = ''
    form.password = ''
  },
)
const route = useRoute(),
  router = useRouter(),
  { busy, error, run } = useTask()
async function submit() {
  await run(async () => {
    const result = await request(
      ({ customer: 'customerLogin', worker: 'workerLogin', admin: 'adminLogin' } as const)[
        props.role
      ],
      { body: { ...form } },
    )
    saveSession(props.role, result)
    const redirect =
      typeof route.query.redirect === 'string' &&
      route.query.redirect.startsWith(`/${props.role}/`) &&
      !route.query.redirect.includes('/login')
        ? route.query.redirect
        : `/${props.role}`
    await router.replace(redirect)
  })
}
</script>
<template>
  <main class="auth">
    <h1>家政预约与调度平台</h1>
    <h2 v-if="role !== 'customer'">{{ roleLabels[role] }}登录</h2>
    <nav v-if="role !== 'customer'" class="actions">
      <RouterLink v-for="(name, key) in roleLabels" :key="key" :to="`/${key}/login`">
        {{ name }}端
      </RouterLink>
    </nav>
    <p v-if="route.query.expired" role="alert">登录失效，请重新登录。</p>
    <form @submit.prevent="submit">
      <label>
        用户名
        <input
          name="username"
          v-model.trim="form.username"
          required
          minlength="3"
          maxlength="32"
          autocomplete="username"
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
          autocomplete="current-password"
        />
      </label>
      <button :disabled="busy" type="submit">登录</button>
    </form>
    <Feedback :error="error" :busy="busy" />
    <RouterLink v-if="role === 'customer'" :to="{ path: '/customer/register', query: route.query }">
      注册客户账号
    </RouterLink>
  </main>
</template>
