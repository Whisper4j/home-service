export async function allPages<T>(
  fetchPage: (pageNo: number) => Promise<{ list: T[]; pages: number }>,
): Promise<T[]> {
  const first = await fetchPage(1),
    result = [...first.list]
  for (let pageNo = 2; pageNo <= first.pages; pageNo++)
    result.push(...(await fetchPage(pageNo)).list)
  return result
}
