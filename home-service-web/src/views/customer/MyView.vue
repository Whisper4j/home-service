<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useMock } from '../../api/client'
import { clearSession, sessions } from '../../stores/session'
const router = useRouter()
function logout() {
  clearSession('customer')
  void router.push('/customer/home')
}
</script>
<template>
  <h2>{{ sessions.customer?.account.displayName || '欢迎使用家政预约' }}</h2>
  <p>{{ sessions.customer ? '已登录' : '尚未登录，可先浏览服务' }}</p>
  <RouterLink
    v-if="!sessions.customer"
    class="button wide-button"
    to="/customer/login?redirect=/customer/me"
  >
    登录 / 注册
  </RouterLink>
  <nav class="menu-list" aria-label="我的服务">
    <RouterLink to="/customer/profile">个人信息 →</RouterLink>
    <RouterLink to="/customer/addresses">地址簿 →</RouterLink>
    <RouterLink to="/customer/rules">服务范围与预约说明 →</RouterLink>
  </nav>
  <button v-if="sessions.customer" class="wide-button" @click="logout">退出登录</button>
  <details v-if="useMock" class="muted">
    <summary>关于产品原型</summary>
    <p>当前操作保存在本浏览器，不代表真实后端已实现；全部支付为模拟。</p>
    <RouterLink to="/customer/development">打开独立演示工具</RouterLink>
  </details>
</template>
