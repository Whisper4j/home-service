<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { request } from '../api/client'
import type { Schema } from '../api/types'
import { saveSession, sessions } from '../stores/session'
import { useTask } from '../composables/useTask'
import { label } from '../utils/labels'
import ModalPanel from './ModalPanel.vue'
import Feedback from './Feedback.vue'
const props = defineProps<{ role: 'customer' | 'worker' }>()
const emit = defineEmits<{ close: []; saved: [] }>()
const person = ref<Schema['AccountVO'] | Schema['WorkerProfileVO']>(),
  editing = ref(false)
const form = reactive({ displayName: '', phone: '' }),
  loader = useTask(),
  action = useTask()
const key = `profile.draft.${props.role}.${sessions[props.role]?.account.id}`
const baseline = ref('')
const dirty = computed(() => editing.value && JSON.stringify(form) !== baseline.value)
watch(
  form,
  () => {
    if (!editing.value) return
    if (dirty.value) sessionStorage.setItem(key, JSON.stringify(form))
    else sessionStorage.removeItem(key)
  },
  { deep: true, flush: 'sync' },
)
async function load() {
  await loader.run(async () => {
    person.value =
      props.role === 'worker'
        ? await request('getWorkerProfile')
        : await request('customerCurrentAccount')
    Object.assign(form, { displayName: person.value.displayName, phone: person.value.phone })
    baseline.value = JSON.stringify(form)
    try {
      const draft = JSON.parse(sessionStorage.getItem(key) || 'null')
      if (draft) {
        Object.assign(form, draft)
        editing.value = true
      }
    } catch {
      sessionStorage.removeItem(key)
    }
  })
}
async function save() {
  await action.run(
    async (idempotencyKey) => {
      const data =
        props.role === 'worker'
          ? await request('updateWorkerContact', {
              body: { phone: form.phone },
              idempotencyKey,
            })
          : await request('updateCustomerProfile', { body: { ...form }, idempotencyKey })
      person.value = data
      const session = sessions[props.role]
      if (session)
        saveSession(props.role, {
          ...session,
          account: { ...session.account, displayName: data.displayName, phone: data.phone },
        })
      baseline.value = JSON.stringify(form)
      sessionStorage.removeItem(key)
      editing.value = false
      emit('saved')
    },
    '个人资料已保存',
    JSON.stringify(form),
  )
}
function discardDraft() {
  sessionStorage.removeItem(key)
}
function close() {
  discardDraft()
  emit('close')
}
onMounted(load)
</script>
<template>
  <ModalPanel
    title="个人资料"
    :dirty="dirty"
    :busy="action.busy.value"
    @discard="discardDraft"
    @close="close"
  >
    <Feedback :busy="loader.busy.value" :error="loader.error.value" />
    <button v-if="loader.error.value" @click="load">重新加载</button>
    <template v-if="person">
      <dl>
        <dt>{{ role === 'worker' ? '姓名' : '称呼' }}</dt>
        <dd>{{ person.displayName }}</dd>
        <dt>账号</dt>
        <dd>{{ person.username }}（只读）</dd>
        <dt>联系电话</dt>
        <dd>{{ person.phone }}</dd>
        <template v-if="'skills' in person">
          <dt>技能</dt>
          <dd>{{ person.skills.map((skill) => skill.name).join('、') }}</dd>
          <dt>城市</dt>
          <dd>广州市</dd>
          <dt>账号状态</dt>
          <dd>{{ label(person.status) }}</dd>
          <dt>调度资格</dt>
          <dd>{{ person.dispatchEnabled ? '允许新分配' : '暂停新分配，已有任务继续履约' }}</dd>
        </template>
      </dl>
      <button v-if="!editing" class="text-action" @click="editing = true">✎ 编辑信息</button>
      <form v-else id="profile-form" @submit.prevent="save">
        <label v-if="role === 'customer'">
          称呼
          <input name="displayName" v-model.trim="form.displayName" required maxlength="40" />
        </label>
        <label>
          联系电话
          <input
            name="phone"
            v-model.trim="form.phone"
            type="tel"
            required
            pattern="1[0-9]{10}"
            maxlength="11"
          />
        </label>
        <p class="muted">
          {{
            role === 'worker'
              ? '仅可修改联系电话，其他资料由平台维护。'
              : '称呼和个人电话不覆盖地址簿或历史订单联系人。'
          }}
        </p>
      </form>
      <Feedback :error="action.error.value" />
    </template>
    <template v-if="editing" #actions>
      <button form="profile-form" :disabled="action.busy.value || !dirty">保存个人信息</button>
    </template>
  </ModalPanel>
</template>
