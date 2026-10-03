import { ref } from 'vue'
import { ApiError, errorMessage } from '../api/errors'
import { createIdempotencyKey } from '../api/client'

export function useTask() {
  const busy = ref(false), error = ref(''), success = ref('')
  let retryKey: string | undefined, retrySignature = ''
  async function run<T>(action: (key: string) => Promise<T>, message = '', signature = ''): Promise<T | undefined> {
    if (busy.value) return undefined
    busy.value = true; error.value = ''; success.value = ''
    if (signature !== retrySignature) retryKey = undefined
    retrySignature = signature
    retryKey ||= createIdempotencyKey()
    try {
      const result = await action(retryKey)
      retryKey = undefined; success.value = message
      return result
    } catch (err) {
      error.value = errorMessage(err)
      if (err instanceof ApiError && err.data.fieldErrors) error.value += '：' + err.data.fieldErrors.map(f => `${f.field} ${f.message}`).join('；')
      if (!(err instanceof ApiError) || err.code !== 'NETWORK_ERROR') retryKey = undefined
      return undefined
    } finally { busy.value = false }
  }
  return { busy, error, success, run }
}
