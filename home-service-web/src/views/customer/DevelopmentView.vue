<script setup lang="ts">
import { defineAsyncComponent } from 'vue'
import { useMock } from '../../api/client'
const DemoPanel = defineAsyncComponent(() => import('../../components/DemoPanel.vue'))
</script>
<template>
  <h2>独立演示工具</h2>
  <p>这些工具只用于产品原型验证，不是正常预约流程。</p>
  <DemoPanel v-if="useMock" />
  <nav class="menu-list">
    <RouterLink to="/customer/login">客户登录</RouterLink>
    <RouterLink to="/worker/login">服务人员独立登录</RouterLink>
    <RouterLink to="/admin/login">管理员独立登录</RouterLink>
  </nav>
</template>
