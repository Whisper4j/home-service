<script setup lang="ts">
import { nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { toast, toastKind, showError } from '../stores/feedback'
import type { Schema } from '../api/types'
const panel = ref<HTMLElement>()
function position() {
  const bounds = document.querySelector('.customer-scroll, main')?.getBoundingClientRect()
  if (!panel.value) return
  panel.value.style.left = `${(bounds?.left || 0) + 12}px`
  panel.value.style.top = `${Math.max(bounds?.top || 0, window.visualViewport?.offsetTop || 0) + 8}px`
  panel.value.style.width = `${Math.max(0, (bounds?.width || window.innerWidth) - 24)}px`
}
watch(toast, async (value) => {
  await nextTick()
  if (value) {
    position()
    panel.value?.hidePopover()
    panel.value?.showPopover()
  } else panel.value?.hidePopover()
})
type Field = HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
function fieldOf(event: Event): Field | undefined {
  return event.target instanceof HTMLInputElement ||
    event.target instanceof HTMLSelectElement ||
    event.target instanceof HTMLTextAreaElement
    ? event.target
    : undefined
}
function clearField(field: Field) {
  field.removeAttribute('aria-invalid')
  document.getElementById(field.dataset.errorId || '')?.remove()
  field.removeAttribute('aria-describedby')
  delete field.dataset.errorId
  delete field.dataset.serverErrorValue
}
let sequence = 0
function fieldError(field: Field, message: string) {
  let error = document.getElementById(field.dataset.errorId || '')
  if (!error) {
    error = document.createElement('small')
    error.dataset.fieldError = ''
    error.className = 'field-error'
    error.id = `field-error-${++sequence}`
    // 错误是描述，不作为父 label 的输入框名称。
    field.setAttribute('aria-describedby', error.id)
    field.dataset.errorId = error.id
    ;(field.closest('label') || field).after(error)
  }
  error.textContent = message
  field.setAttribute('aria-invalid', 'true')
}
function serverErrors(event: Event) {
  const errors = (event as CustomEvent<Schema['ErrorDetailsVO']['fieldErrors']>).detail || []
  for (const error of errors) {
    const name = error.field.split(/[./]/).filter(Boolean).at(-1)
    const fields = document.querySelectorAll<Field>('input[name], select[name], textarea[name]')
    for (const field of fields)
      if (field.name === name) {
        fieldError(field, error.message)
        field.dataset.serverErrorValue = field.value
      }
  }
}
function validate(event: Event) {
  const field = fieldOf(event)
  if (!field || field.disabled || field.dataset.quote !== undefined) return
  if (field.validity.valid) {
    if (field.dataset.serverErrorValue === field.value) return
    clearField(field)
    return
  }
  event.preventDefault()
  const message = field.validity.valueMissing
    ? '请填写此项'
    : field.validity.patternMismatch
      ? '填写格式不正确，请检查'
      : field.validationMessage
  fieldError(field, message)
  showError(message)
}
function correct(event: Event) {
  const field = fieldOf(event)
  if (field?.validity.valid && field.dataset.serverErrorValue !== field.value) clearField(field)
}
onMounted(() => {
  document.addEventListener('invalid', validate, true)
  document.addEventListener('blur', validate, true)
  document.addEventListener('input', correct, true)
  document.addEventListener('change', correct, true)
  window.addEventListener('resize', position)
  window.addEventListener('field-errors', serverErrors)
  window.visualViewport?.addEventListener('resize', position)
})
onUnmounted(() => {
  document.removeEventListener('invalid', validate, true)
  document.removeEventListener('blur', validate, true)
  document.removeEventListener('input', correct, true)
  document.removeEventListener('change', correct, true)
  window.removeEventListener('resize', position)
  window.removeEventListener('field-errors', serverErrors)
  window.visualViewport?.removeEventListener('resize', position)
})
</script>
<template>
  <div
    ref="panel"
    popover="manual"
    class="error-toast"
    :role="toastKind === 'success' ? 'status' : 'alert'"
  >
    {{ toast }}
  </div>
</template>
<style>
.error-toast {
  position: fixed;
  inset: auto;
  margin: 0;
  padding: 0.8rem 1rem;
  border: 2px solid #333;
  background: white;
  color: #111;
  box-shadow: 0 3px 12px #0003;
  pointer-events: none;
  overflow-wrap: anywhere;
  max-height: 35dvh;
  overflow: auto;
  z-index: 10000;
}
.field-error {
  display: block;
  margin-top: 0.3rem;
  font-size: 0.85rem;
  font-weight: 600;
}
[aria-invalid='true'] {
  outline: 2px solid #555;
  outline-offset: 1px;
}
</style>
