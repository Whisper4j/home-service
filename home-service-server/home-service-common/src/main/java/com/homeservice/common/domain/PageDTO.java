package com.homeservice.common.domain;
import java.util.List;
/** list 可为空页，但 total/pages 必须来自完整计数。 */
public record PageDTO<T>(List<T> list, long total, long pages) {
    public PageDTO {
        list = List.copyOf(list);
        if (total < 0 || pages < 0) throw new IllegalArgumentException("分页计数不可为负");
    }
    public static <T> PageDTO<T> of(List<T> list, long total, long pageSize) {
        if (pageSize < 1) throw new IllegalArgumentException("pageSize 必须为正");
        return new PageDTO<>(list, total, total / pageSize + (total % pageSize == 0 ? 0 : 1));
    }
}
