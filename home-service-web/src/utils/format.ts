export function cents(value: string): number {
  if (!/^(0|[1-9][0-9]{0,8})\.[0-9]{2}$/.test(value)) throw new Error('金额须为两位小数字符串')
  const [yuan, fraction] = value.split('.')
  return Number(yuan) * 100 + Number(fraction)
}
export function money(value: number): string {
  if (!Number.isSafeInteger(value) || value < 0) throw new Error('金额分值无效')
  return `${Math.floor(value / 100)}.${String(value % 100).padStart(2, '0')}`
}
export const HOUR = 3_600_000
export const DAY = 24 * HOUR
export const SLOT = HOUR / 2
export function iso(time: number): string {
  return new Date(time + 8 * HOUR).toISOString().slice(0, 19) + '+08:00'
}
export function dateOf(time: number): string {
  return iso(time).slice(0, 10)
}
export function displayTime(value?: string): string {
  return value ? value.replace('T', ' ').replace('+08:00', '') : '—'
}
export function localInput(time: number): string {
  return iso(time).slice(0, 16)
}
export function fromLocalInput(value: string): string {
  return `${value}:00+08:00`
}
export function tomorrowMorning(now = Date.now(), days = 1): number {
  return Date.parse(`${dateOf(now + days * DAY)}T09:00:00+08:00`)
}
