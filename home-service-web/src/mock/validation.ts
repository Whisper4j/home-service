import Ajv from 'ajv'
import addFormats from 'ajv-formats'
import schemas from '../api/generated/schemas.json' with { type: 'json' }
import { fail } from '../api/errors'

const ajv = new Ajv({ allErrors: true, strict: false })
addFormats(ajv)
ajv.addSchema({ $id: 'contract', components: { schemas } })
export function validateSchema(name: string, value: unknown): void {
  const validate =
    ajv.getSchema(`contract#/components/schemas/${name}`) ||
    ajv.compile({ $ref: `contract#/components/schemas/${name}` })
  if (!validate(value))
    fail('VALIDATION_ERROR', '输入格式不符合接口契约', 400, {
      fieldErrors: (validate.errors || []).map((e) => ({
        field: e.instancePath || '/',
        message: `${e.message || '字段无效'} ${e.params.missingProperty || ''}`.trim(),
      })),
    })
}
