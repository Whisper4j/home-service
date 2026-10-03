import type { Schema } from '../api/types'
import { fail } from '../api/errors'
import type { MockContext } from './context'
import { canAssign } from './scheduling'
import { iso } from '../utils/format'
export async function inspectImage(file?: File): Promise<string> {
  if (!file || file.size === 0) fail('VALIDATION_ERROR', '请选择有效图片', 400)
  if (file.size > 5 * 1024 * 1024) fail('IMAGE_TOO_LARGE', '单张图片不能超过 5 MB', 413)
  const bytes = new Uint8Array(await file.arrayBuffer())
  const png = bytes[0] === 137 && bytes[1] === 80 && bytes[2] === 78 && bytes[3] === 71
  const jpeg = bytes[0] === 255 && bytes[1] === 216 && bytes[2] === 255
  const webp =
    new TextDecoder().decode(bytes.slice(0, 4)) === 'RIFF' &&
    new TextDecoder().decode(bytes.slice(8, 12)) === 'WEBP'
  if (!(
    (file.type === 'image/png' && png) ||
    (file.type === 'image/jpeg' && jpeg) ||
    (file.type === 'image/webp' && webp)
  ))
    fail('IMAGE_TYPE_UNSUPPORTED', '仅支持有效的 JPG、PNG 或 WebP 图片', 415)
  if (typeof createImageBitmap === 'function') {
    let bitmap: ImageBitmap
    try {
      bitmap = await createImageBitmap(file)
    } catch {
      fail('IMAGE_TYPE_UNSUPPORTED', '图片内容损坏或无法读取', 415)
    }
    const pixels = bitmap.width * bitmap.height
    bitmap.close()
    if (pixels > 40_000_000) fail('IMAGE_TOO_LARGE', '图片最多支持4000万像素', 413)
  }
  return Array.from(new Uint8Array(await crypto.subtle.digest('SHA-256', bytes)), (b) =>
    b.toString(16).padStart(2, '0'),
  ).join('')
}
export function uploadImage(
  context: MockContext,
  ownerId: string,
  file: File,
): Schema['SceneImageVO'] {
  const image: Schema['SceneImageVO'] = {
    id: context.nextId(),
    mimeType: file.type as Schema['SceneImageVO']['mimeType'],
    size: file.size,
    createdAt: iso(context.now),
  }
  context.db.images.push({ ...image, ownerId })
  return image
}
export function authorizeImage(
  context: MockContext,
  account: Schema['AccountVO'],
  id: string,
  orderId?: string,
): Schema['SceneImageVO'] {
  const image = context.db.images.find((i) => i.id === id)
  const order = context.db.orders.find((s) => s.order.id === orderId)?.order
  const linked = order?.sceneImages.some((i) => i.id === id)
  const allowed =
    account.role === 'CUSTOMER'
      ? image?.ownerId === account.id && (!orderId || (linked && order?.customerId === account.id))
      : linked &&
        (account.role === 'ADMIN' ||
          order?.workerId === account.id ||
          (order?.status === 'WAITING_ACCEPTANCE' &&
            context.db.workers.some(
              (w) => w.accountId === account.id && canAssign(context, w, order),
            )))
  if (!image || !allowed) fail('NOT_FOUND', '图片不存在或不可访问', 404)
  const { ownerId: _owner, ...view } = image
  return view
}
export function imageStore<T>(operation: (store: IDBObjectStore) => IDBRequest<T>): Promise<T> {
  return new Promise((resolve, reject) => {
    const open = indexedDB.open('home-service-images', 1)
    open.onupgradeneeded = () => open.result.createObjectStore('images')
    open.onerror = () => reject(open.error)
    open.onsuccess = () => {
      const db = open.result,
        transaction = db.transaction('images', 'readwrite')
      const result = operation(transaction.objectStore('images'))
      transaction.oncomplete = () => {
        db.close()
        resolve(result.result)
      }
      transaction.onerror = () => {
        db.close()
        reject(transaction.error)
      }
      transaction.onabort = () => {
        db.close()
        reject(transaction.error)
      }
    }
  })
}
