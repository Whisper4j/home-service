import type { components, operations } from './generated/schema'
export type Schema = components['schemas']
export type Role = Schema['Role']
export type RolePath = 'customer' | 'worker' | 'admin'
export type OperationId = keyof operations
type JsonBody<T> = T extends { content: { 'application/json': infer B } } ? B : never
export type Input<K extends OperationId> = operations[K] extends { requestBody: infer B }
  ? JsonBody<B>
  : never
export type Query<K extends OperationId> = operations[K] extends { parameters: { query?: infer Q } }
  ? Q
  : never
type SuccessResponse<T> = T extends { 200: infer R } ? R : T extends { 201: infer R } ? R : never
export type Output<K extends OperationId> =
  SuccessResponse<operations[K]['responses']> extends { content: { 'image/jpeg': string } }
    ? Blob
    : JsonBody<SuccessResponse<operations[K]['responses']>> extends { data: infer D }
      ? D
      : never
export type RequestOptions<K extends OperationId> = {
  id?: string
  body?: Input<K>
  file?: K extends 'uploadSceneImage' ? File : never
  query?: Query<K>
  idempotencyKey?: string
  signal?: AbortSignal
}
