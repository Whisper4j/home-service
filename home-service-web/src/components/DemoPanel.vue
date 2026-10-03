<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { advanceClock, mockNow, resetDemo, simulateNetworkFailure, tickMock } from '../mock/transport'
import { clearAllSessions } from '../stores/session'
import { displayTime, iso } from '../utils/format'
import { useTask } from '../composables/useTask'
import Feedback from './Feedback.vue'
const current = ref(iso(mockNow())), { busy, error, success, run } = useTask()
let timer: ReturnType<typeof setInterval>
async function advance(minutes: number) { await run(async () => { await advanceClock(minutes); current.value = iso(mockNow()); window.dispatchEvent(new Event('data-refresh')) }, '演示时间已推进') }
async function reset() {
  if (!window.confirm('重置将清空本浏览器全部演示操作并退出三端账号，是否继续？')) return
  await resetDemo(); clearAllSessions(); location.assign('/customer/login')
}
onMounted(() => { timer = setInterval(async () => { await tickMock(); current.value = iso(mockNow()) }, 10_000) })
onUnmounted(() => clearInterval(timer))
</script>
<template>
  <details class="demo-panel"><summary>演示模式 · {{ displayTime(current) }}</summary>
    <p>独立虚构数据，三端可在不同标签页联动。下列工具仅用于演示；正式接口模式不会显示。</p>
    <div class="actions"><button v-for="[minutes, title] in [[0.5, '+30秒'], [5, '+5分钟'], [15, '+15分钟'], [120, '+2小时'], [1440, '+24小时']]" :key="title" :disabled="busy" @click="advance(Number(minutes))">{{ title }}</button><button @click="simulateNetworkFailure(); success = '下一次请求将模拟网络中断'">下次请求模拟断网</button><button @click="reset">重置演示数据</button></div>
    <p>推进时间会触发到期任务；时间推进后若登录到期，请重新登录。</p><Feedback :error="error" :success="success" :busy="busy" />
  </details>
</template>
