<script setup lang="ts">
import { onUnmounted, ref, watch } from 'vue'
import { request } from '../api/client'
import type { RolePath, Schema } from '../api/types'
const props = defineProps<{
  images: Schema['SceneImageVO'][]
  role: RolePath
  orderId?: string
  removable?: boolean
}>()
const emit = defineEmits<{ remove: [id: string] }>()
const urls = ref<Record<string, string>>({}),
  errors = ref<Record<string, string>>({}),
  loading = ref<Record<string, boolean>>({})
const preview = ref<HTMLDialogElement>(),
  selected = ref('')
let generation = 0
function clear() {
  Object.values(urls.value).forEach(URL.revokeObjectURL)
  urls.value = {}
  selected.value = ''
  preview.value?.close()
}
async function load(id: string, current = generation) {
  loading.value[id] = true
  delete errors.value[id]
  try {
    const blob = await request(
      (
        {
          customer: 'customerGetSceneImage',
          worker: 'workerGetSceneImage',
          admin: 'adminGetSceneImage',
        } as const
      )[props.role],
      { id, query: props.orderId ? { orderId: props.orderId } : {} },
    )
    if (current !== generation) return
    if (urls.value[id]) URL.revokeObjectURL(urls.value[id])
    urls.value[id] = URL.createObjectURL(blob)
  } catch (error) {
    if (current === generation)
      errors.value[id] = error instanceof Error ? error.message : '图片加载失败'
  } finally {
    if (current === generation) loading.value[id] = false
  }
}
function open(id: string) {
  selected.value = id
  preview.value?.showModal()
}
function close() {
  preview.value?.close()
}
function remove(id: string) {
  emit('remove', id)
}
watch(
  () => `${props.role}:${props.orderId}:${props.images.map((i) => i.id).join(',')}`,
  () => {
    generation++
    clear()
    props.images.forEach((i) => void load(i.id))
  },
  { immediate: true },
)
onUnmounted(() => {
  generation++
  clear()
})
</script>
<template>
  <div v-if="images.length" class="image-grid">
    <figure v-for="(image, index) in images" :key="image.id">
      <button
        v-if="urls[image.id]"
        :aria-label="`预览现场图片 ${index + 1}`"
        @click="open(image.id)"
        type="button"
      >
        <img :src="urls[image.id]" :alt="`现场图片 ${index + 1}`" />
      </button>
      <p v-if="loading[image.id]" class="muted">图片加载中…</p>
      <template v-if="errors[image.id]">
        <p role="alert" class="muted">{{ errors[image.id] }}</p>
        <button type="button" @click="load(image.id)">重试查看</button>
      </template>
      <button
        v-if="removable"
        type="button"
        :aria-label="`删除现场图片 ${index + 1}`"
        @click="remove(image.id)"
      >
        删除
      </button>
    </figure>
  </div>
  <dialog ref="preview" class="image-preview" aria-label="现场图片预览">
    <button type="button" @click="close">关闭预览</button>
    <img v-if="selected && urls[selected]" :src="urls[selected]" alt="现场图片大图" />
    <p class="muted">客户主动提供的现场情况，仅用于本次服务判断。</p>
  </dialog>
</template>
<style scoped>
.image-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 0.6rem;
  max-width: 420px;
  margin-block: 0.7rem;
}
.image-grid figure {
  margin: 0;
  min-width: 0;
}
.image-grid button {
  width: 100%;
  padding: 0.3rem;
}
.image-grid img {
  display: block;
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
}
.image-preview img {
  display: block;
  max-width: 100%;
  max-height: 65dvh;
  object-fit: contain;
  margin-block: 0.8rem;
}
</style>
