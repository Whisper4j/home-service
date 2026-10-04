<script setup lang="ts">
import { useRouter } from 'vue-router'
import { clearSession } from '../stores/session'
import ModalPanel from './ModalPanel.vue'
const props = defineProps<{ role: 'customer' | 'worker' }>()
defineEmits<{ close: [] }>()
const router = useRouter()
function logout() {
  clearSession(props.role)
  void router.replace(props.role === 'customer' ? '/customer/home' : '/worker/login')
}
</script>
<template>
  <ModalPanel title="退出登录" @close="$emit('close')">
    <p>确认退出当前账号？</p>
    <template #actions>
      <button @click="$emit('close')">取消</button>
      <button @click="logout">确认退出</button>
    </template>
  </ModalPanel>
</template>
