<script setup lang="ts">
import { reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { RolePath, Schema } from '../../api/types'
import { request, useMock } from '../../api/client'
import { saveSession } from '../../stores/session'
import { roleLabels } from '../../utils/labels'
import { useTask } from '../../composables/useTask'
import Feedback from '../../components/Feedback.vue'
const props = defineProps<{ role: RolePath }>()
const form = reactive<Schema['LoginDTO']>({
  username: useMock ? props.role : '',
  password: useMock ? 'Demo12345' : '',
})
watch(
  () => props.role,
  (role) => {
    form.username = useMock ? role : ''
    form.password = useMock ? 'Demo12345' : ''
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
    await router.push(redirect)
  })
}
</script>
<template>
  <main class="auth">
    <h1>家政预约与调度平台</h1>
    <h2>{{ roleLabels[role] }}登录</h2>
    <nav class="actions">
      <RouterLink v-for="(name, key) in roleLabels" :key="key" :to="`/${key}/login`">
        {{ name }}端
      </RouterLink>
    </nav>
    <p v-if="route.query.expired" role="alert">登录失效，请重新登录。</p>
    <form @submit.prevent="submit">
      <label>
        用户名
        <input
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
    <RouterLink v-if="role === 'customer'" to="/customer/register">注册客户账号</RouterLink>
    <section v-if="useMock" class="panel">
      <p>
        当前为 Mock 原型，演示密码统一为
        <code>Demo12345</code>
        。
      </p>
      <p>
        客户：customer / customer2
        <br />
        人员：worker / worker2 / repair
        <br />
        管理员：admin
      </p>
      <p>人员由管理员创建，不提供人员自助注册。</p>
    </section>
  </main>
</template>
