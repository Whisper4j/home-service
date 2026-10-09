import { ref } from 'vue'
import { ApiError, errorMessage } from '../api/errors'
import { createIdempotencyKey } from '../api/client'
import type { Schema } from '../api/types'
import { showError, showSuccess } from '../stores/feedback'

export function useTask() {
  const busy = ref(false),
    error = ref(''),
    success = ref(''),
    details = ref<Schema['ErrorDetailsVO']>({}),
    code = ref('')
  let retryKey: string | undefined,
    retrySignature = ''
  async function run<T>(
    action: (key: string) => Promise<T>,
    message = '',
    signature = '',
  ): Promise<T | undefined> {
    if (busy.value) return undefined
    busy.value = true
    error.value = ''
    details.value = {}
    code.value = ''
    success.value = ''
    if (signature !== retrySignature) retryKey = undefined
    retrySignature = signature
    retryKey ||= createIdempotencyKey()
    try {
      const result = await action(retryKey)
      retryKey = undefined
      success.value = message
      if (message) showSuccess(message)
      return result
    } catch (err) {
      error.value = errorMessage(err)
      if (err instanceof ApiError) {
        details.value = err.data
        code.value = err.code
      }
      if (err instanceof ApiError && err.data.fieldErrors) {
        const messages = [...new Set(err.data.fieldErrors.map((fieldError) => fieldError.message))]
        error.value += '：' + messages.join('；')
        window.dispatchEvent(new CustomEvent('field-errors', { detail: err.data.fieldErrors }))
      }
      if (!(err instanceof ApiError) || err.code !== 'NETWORK_ERROR') retryKey = undefined
      showError(error.value)
      return undefined
    } finally {
      busy.value = false
    }
  }
  return { busy, error, success, details, code, run }
}
