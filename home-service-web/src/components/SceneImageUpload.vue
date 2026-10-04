<script setup lang="ts">
import { onUnmounted, ref, watch } from 'vue'
import { createIdempotencyKey, request } from '../api/client'
import type { Schema } from '../api/types'
import SceneImages from './SceneImages.vue'
import { useTask } from '../composables/useTask'
import Feedback from './CustomerFeedback.vue'
import { showError } from '../stores/feedback'
const props = defineProps<{ rules: Schema['BookingRulesVO'] }>()
const model = defineModel<Schema['SceneImageVO'][]>({ required: true })
const emit = defineEmits<{ pending: [value: boolean] }>()
type PendingImage = {
  key: string
  original: File
  clean?: File
  url: string
  busy: boolean
  error: string
}
const pending = ref<PendingImage[]>([]),
  error = ref('')
const { busy: deleting, error: deleteError, run: deleteRun } = useTask()
let disposed = false
watch([pending, deleting], () => emit('pending', pending.value.length > 0 || deleting.value), {
  deep: true,
})
async function sanitize(file: File): Promise<File> {
  if (file.size > props.rules.sceneImageMaxBytes)
    throw Error(`单张图片不能超过 ${props.rules.sceneImageMaxBytes / 1024 / 1024} MiB`)
  if (!props.rules.sceneImageMimeTypes.some((type) => type === file.type))
    throw Error('仅支持 JPG、PNG、WebP 图片')
  let bitmap: ImageBitmap
  try {
    bitmap = await createImageBitmap(file)
  } catch {
    throw Error('图片内容损坏或无法读取，请重新选择')
  }
  try {
    if (bitmap.width * bitmap.height > 40_000_000)
      throw Error('图片分辨率过大，请选择较小的现场照片')
    const canvas = document.createElement('canvas')
    canvas.width = bitmap.width
    canvas.height = bitmap.height
    canvas.getContext('2d')!.drawImage(bitmap, 0, 0)
    const blob = await new Promise<Blob>((resolve, reject) =>
      canvas.toBlob((b) => (b ? resolve(b) : reject(Error('图片处理失败'))), file.type, 0.9),
    )
    if (blob.size > props.rules.sceneImageMaxBytes)
      throw Error('处理后的图片超过接口允许的大小，请选择较小图片')
    return new File([blob], 'scene-image', { type: blob.type })
  } finally {
    bitmap.close()
  }
}
async function upload(row: PendingImage) {
  if (row.busy) return
  row.busy = true
  row.error = ''
  try {
    row.clean ||= await sanitize(row.original)
    const image = await request('uploadSceneImage', { file: row.clean, idempotencyKey: row.key })
    if (disposed) return
    model.value = [...model.value, image]
    removePending(row.key)
  } catch (err) {
    row.error = err instanceof Error ? err.message : '上传失败，请重试'
    showError(row.error)
  } finally {
    row.busy = false
  }
}
async function select(event: Event) {
  const input = event.target as HTMLInputElement,
    files = Array.from(input.files || [])
  input.value = ''
  error.value = ''
  if (files.length + pending.value.length + model.value.length > props.rules.sceneImageMaxCount) {
    error.value = `最多上传 ${props.rules.sceneImageMaxCount} 张现场图片，请删除后再选择`
    showError(error.value)
    return
  }
  for (const file of files) {
    const row: PendingImage = {
      key: createIdempotencyKey(),
      original: file,
      url: URL.createObjectURL(file),
      busy: false,
      error: '',
    }
    pending.value.push(row)
    await upload(pending.value[pending.value.length - 1])
  }
}
function removePending(key: string) {
  const row = pending.value.find((r) => r.key === key)
  if (row) URL.revokeObjectURL(row.url)
  pending.value = pending.value.filter((r) => r.key !== key)
}
function removeUploaded(id: string) {
  void deleteRun(
    async (key) => {
      await request('deleteSceneImage', { id, idempotencyKey: key })
      model.value = model.value.filter((i) => i.id !== id)
    },
    '',
    `delete-image:${id}`,
  )
}
onUnmounted(() => {
  disposed = true
  pending.value.forEach((r) => URL.revokeObjectURL(r.url))
})
</script>
<template>
  <label>
    选择现场图片（可选，最多{{ rules.sceneImageMaxCount }}张）
    <input
      type="file"
      :accept="rules.sceneImageMimeTypes.join(',')"
      multiple
      @change="select"
      :disabled="pending.some((r) => r.busy)"
    />
  </label>
  <p class="muted">
    每张不超过{{ rules.sceneImageMaxBytes / 1024 / 1024 }}
    MiB。请避免拍入个人隐私；上传前清除照片元数据。图片不保证接单、不扩大套餐范围，也不允许现场议价。
  </p>
  <p v-if="error" role="status">{{ error }}</p>
  <SceneImages
    :images="model"
    role="customer"
    removable
    :disabled="deleting"
    @remove="removeUploaded"
  />
  <Feedback :error="deleteError" :busy="deleting" />
  <div v-for="row in pending" :key="row.key" class="panel">
    <img :src="row.url" alt="待上传现场图片" style="width: 80px; height: 80px; object-fit: cover" />
    <p v-if="row.busy" role="status">正在上传…</p>
    <p v-if="row.error" role="status">上传失败：{{ row.error }}</p>
    <div class="actions">
      <button v-if="row.error" type="button" @click="upload(row)">重试上传</button>
      <button :disabled="row.busy" type="button" @click="removePending(row.key)">
        移除待上传图片
      </button>
    </div>
  </div>
</template>
