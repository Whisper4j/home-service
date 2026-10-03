<script setup lang="ts">
defineProps<{ pageNo: number; pageSize: number; total: number; pages: number; busy?: boolean }>()
const emit = defineEmits<{ change: [page: number]; resize: [size: number] }>()
</script>
<template>
  <div class="pagination">
    <span>共 {{ total }} 条 · 第 {{ pages ? pageNo : 0 }} / {{ pages }} 页</span>
    <button :disabled="busy || pageNo <= 1" @click="emit('change', pageNo - 1)">上一页</button>
    <button :disabled="busy || pageNo >= pages" @click="emit('change', pageNo + 1)">下一页</button>
    <label>每页 <select :value="pageSize" :disabled="busy" @change="emit('resize', Number(($event.target as HTMLSelectElement).value))"><option :value="5">5</option><option :value="20">20</option><option :value="50">50</option><option :value="100">100</option></select></label>
  </div>
</template>
