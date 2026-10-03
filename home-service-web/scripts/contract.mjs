import fs from 'node:fs/promises'
import { fileURLToPath } from 'node:url'
import SwaggerParser from '@apidevtools/swagger-parser'
import openapiTS, { astToString } from 'openapi-typescript'
import YAML from 'yaml'

const url = new URL('../../docs/api/openapi.yaml', import.meta.url)
await SwaggerParser.validate(fileURLToPath(url))
const spec = YAML.parse(await fs.readFile(url, 'utf8'))
const ids = new Set()
const routes = {}
for (const [path, methods] of Object.entries(spec.paths)) {
  if (!/^\/(customer|worker|admin)\//.test(path)) throw Error(`非法三端前缀 ${path}`)
  for (const [method, operation] of Object.entries(methods)) {
    if (ids.has(operation.operationId)) throw Error('重复 operationId')
    ids.add(operation.operationId)
    const schemaName = (value) => value?.$ref?.split('/').at(-1)
    const status = Object.keys(operation.responses).find((code) => code.startsWith('2'))
    routes[operation.operationId] = {
      method: method.toUpperCase(),
      path,
      status: Number(status),
      anonymous: operation.security?.length === 0,
      input: schemaName(operation.requestBody?.content['application/json'].schema) || '',
      output: schemaName(operation.responses[status].content['application/json'].schema),
      query: operation['x-query-schema'] || '',
      idempotent: (operation.parameters || []).some((p) => p.$ref?.endsWith('/IdempotencyKey')),
    }
  }
}
const content = astToString(await openapiTS(spec))
const outputs = {
  'schema.ts': content,
  'routes.ts': `// 由 docs/api/openapi.yaml 生成，禁止手改。\nexport const routes = ${JSON.stringify(routes, null, 2)} as const\n`,
  'schemas.json': `${JSON.stringify(spec.components.schemas, null, 2)}\n`,
}
const directory = new URL('../src/api/generated/', import.meta.url)
await fs.mkdir(directory, { recursive: true })
for (const [name, text] of Object.entries(outputs)) {
  const target = new URL(name, directory)
  if (process.argv.includes('--write')) await fs.writeFile(target, text)
  else if ((await fs.readFile(target, 'utf8')) !== text)
    throw Error(`${name} 与契约不一致，请运行 npm run api:generate`)
}
console.log(`OpenAPI 3.0.3: ${ids.size} operations, syntax/references/generated files OK`)
