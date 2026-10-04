<script setup lang="ts">
import { useRouter } from 'vue-router'
import { sessions } from '../../stores/session'
import { usePanel } from '../../composables/usePanel'
import ProfilePanel from '../../components/ProfilePanel.vue'
import AddressPanel from '../../components/AddressPanel.vue'
import ModalPanel from '../../components/ModalPanel.vue'
import LogoutPanel from '../../components/LogoutPanel.vue'
import CustomerRules from '../../components/CustomerRules.vue'
const router = useRouter(),
  { panel, open, close } = usePanel()
function authenticatedPanel(name: string) {
  if (!sessions.customer)
    void router.push({ path: '/customer/login', query: { redirect: `/customer/me?panel=${name}` } })
  else void open(name)
}
</script>
<template>
  <section class="identity">
    <h2>
      <button class="text-action identity-name" @click="authenticatedPanel('profile')">
        {{ sessions.customer?.account.displayName || '欢迎使用家政预约' }}
      </button>
    </h2>
    <p class="muted">{{ sessions.customer ? '已登录 · 客户' : '尚未登录，可先浏览服务' }}</p>
    <RouterLink v-if="!sessions.customer" to="/customer/login?redirect=/customer/me">
      登录 / 注册
    </RouterLink>
  </section>
  <nav class="compact-links" aria-label="我的服务">
    <button class="text-action" @click="authenticatedPanel('addresses')">地址簿 →</button>
    <button class="text-action" @click="open('rules')">服务范围与预约说明 →</button>
    <button v-if="sessions.customer" class="text-action" @click="open('logout')">退出登录</button>
  </nav>
  <ProfilePanel v-if="sessions.customer && panel === 'profile'" role="customer" @close="close" />
  <AddressPanel v-if="sessions.customer && panel === 'addresses'" @close="close" />
  <ModalPanel v-if="panel === 'rules'" title="服务范围与预约说明" @close="close">
    <CustomerRules />
  </ModalPanel>
  <LogoutPanel v-if="sessions.customer && panel === 'logout'" role="customer" @close="close" />
</template>
