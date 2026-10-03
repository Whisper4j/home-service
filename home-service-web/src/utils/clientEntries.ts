import type { Schema } from '../api/types'
export const clientEntries: {
  code: Schema['ClientEntryCode']
  group: string
  title: string
  hint: string
}[] = [
  {
    code: 'DAILY_2H',
    group: 'daily',
    title: '2 小时套餐',
    hint: '小户型或局部打扫，参考约 60㎡以内',
  },
  { code: 'DAILY_3H', group: 'daily', title: '3 小时套餐', hint: '常见中小户型，参考约 60～90㎡' },
  {
    code: 'DAILY_4H',
    group: 'daily',
    title: '4 小时套餐',
    hint: '较大户型或内容较多，参考约 90～120㎡',
  },
  { code: 'DEEP_60', group: 'deep', title: '60㎡以内', hint: '已入住小户型，重点处理厨卫和油污' },
  { code: 'DEEP_100', group: 'deep', title: '61～100㎡', hint: '已入住中等户型，重点区域精细清洁' },
  {
    code: 'TOILET_UNBLOCK',
    group: 'plumbing',
    title: '马桶疏通',
    hint: '常见软性堵塞，不含拆装和主管道改造',
  },
  {
    code: 'TOILET_VALVE',
    group: 'plumbing',
    title: '马桶进水阀维修',
    hint: '固定进水阀项目，客户自备适配配件',
  },
  {
    code: 'TAP_REPAIR',
    group: 'plumbing',
    title: '水龙头密封件维修',
    hint: '普通漏水密封件处理，客户自备配件',
  },
  {
    code: 'TAP_REPLACE',
    group: 'plumbing',
    title: '更换同规格水龙头',
    hint: '客户自备同规格水龙头，不含管道改造',
  },
  {
    code: 'BULB_REPLACE',
    group: 'electrical',
    title: '更换灯泡',
    hint: '普通可触及灯泡，客户自备适配灯泡',
  },
  {
    code: 'LIGHT_REPLACE',
    group: 'electrical',
    title: '更换普通灯具',
    hint: '客户自备同类灯具，不含大型吊灯和改线',
  },
  {
    code: 'FUSE_REPLACE',
    group: 'electrical',
    title: '更换同规格保险丝',
    hint: '客户自备配件，不含故障检测与线路维修',
  },
  {
    code: 'AC_CLEAN',
    group: 'appliance',
    title: '壁挂空调清洗',
    hint: '常规室内机清洗，不含加氟和故障检修',
  },
]
// 显式业务标识；判断关联有效性不依赖目录名称或顺序。
export function matchesEntry(
  code: Schema['ClientEntryCode'],
  kind: Schema['ServiceKind'],
  duration: number,
): boolean {
  if (code.startsWith('DAILY_'))
    return (
      kind === 'CLEANING' &&
      duration === ({ DAILY_2H: 120, DAILY_3H: 180, DAILY_4H: 240 } as Record<string, number>)[code]
    )
  return code.startsWith('DEEP_') ? kind === 'CLEANING' : kind === 'REPAIR'
}
