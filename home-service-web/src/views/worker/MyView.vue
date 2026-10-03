<script setup lang="ts">
import { useRouter } from 'vue-router'
import { sessions, clearSession } from '../../stores/session'
const router = useRouter()
function logout() {
  clearSession('worker')
  void router.replace('/worker/login')
}
</script>
<template>
  <h2>{{ sessions.worker?.account.displayName }}</h2>
  <p>已登录 · 服务人员</p>
  <nav class="menu-list">
    <RouterLink to="/worker/profile">个人资料</RouterLink>
    <RouterLink to="/worker/statistics">服务数据</RouterLink>
    <RouterLink to="/worker/schedule">工作时间与请假</RouterLink>
    <RouterLink to="/worker/rules">服务规则</RouterLink>
  </nav>
  <button class="wide-button" @click="logout">退出登录</button>
</template>
