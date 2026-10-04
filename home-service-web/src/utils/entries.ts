import type { Schema } from '../api/types'
type Entry = Schema['ClientEntryVO']
export function sortedEntries(entries: readonly Entry[]): Entry[] {
  return [...entries].sort(
    (a, b) =>
      a.groupSort - b.groupSort ||
      a.groupCode.localeCompare(b.groupCode) ||
      a.sort - b.sort ||
      a.code.localeCompare(b.code),
  )
}
export function entryGroups(entries: readonly Entry[]): Entry[] {
  return [...new Map(sortedEntries(entries).map((entry) => [entry.groupCode, entry])).values()]
}
