<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const choice = ref<HTMLDialogElement>(),
  router = useRouter()
function openCleaning() {
  choice.value?.showModal()
}
function closeChoice() {
  choice.value?.close()
}
function choose(kind: string) {
  closeChoice()
  void router.push(`/customer/cleaning/${kind}`)
}
function openRepair() {
  void router.push('/customer/repair')
}
</script>
<template>
  <div class="home-content">
    <div>
      <h2>家政预约与调度平台</h2>
      <p class="muted">当前服务范围：广州市</p>
      <p>选好服务，再安排上门时间。</p>
    </div>
    <div class="home-entries">
      <button class="entry-button" @click="openCleaning">
        <strong>清洁服务</strong>
        <span>日常清洁、深度清洁</span>
        <span>按需求选套餐，再选预约方式 →</span>
      </button>
      <button class="entry-button" @click="openRepair">
        <strong>维修服务</strong>
        <span>管道卫浴、电气灯具、家电维护</span>
        <span>范围明确、固定价格 →</span>
      </button>
    </div>
    <p class="muted">先浏览，再预约。支付后安排人员，履约进度随时查看。</p>
  </div>
  <dialog ref="choice" class="customer-dialog" aria-labelledby="cleaning-choice">
    <h2 id="cleaning-choice">你需要哪一种清洁？</h2>
    <button class="entry-button" @click="choose('daily')">
      <strong>日常清洁</strong>
      <span>适合日常维护，常规地面、表面和厨卫清洁。</span>
    </button>
    <button class="entry-button" @click="choose('deep')">
      <strong>深度清洁</strong>
      <span>适合较久未彻底清洁，重点处理厨卫和油污等区域。</span>
    </button>
    <p class="muted">深度清洁不包含装修后的开荒保洁。</p>
    <button class="wide-button" @click="closeChoice">暂不选择</button>
  </dialog>
</template>
