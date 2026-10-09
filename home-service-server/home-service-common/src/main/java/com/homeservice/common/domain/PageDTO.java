package com.homeservice.common.domain;

import com.homeservice.common.constant.MessageConstant;

import java.util.List;

import lombok.Data;

@Data
public class PageDTO<T> {

    private List<T> list; // 当前页数据
    private long total; // 总记录数
    private long pages; // 总页数

    public PageDTO(List<T> list, long total, long pages) {
        this.list = List.copyOf(list);
        if (total < 0 || pages < 0)
            throw new IllegalArgumentException(MessageConstant.PAGINATION_TOTAL_INVALID);
        this.total = total;
        this.pages = pages;
    }

    public static <T> PageDTO<T> of(List<T> list, long total, long pageSize) {
        if (pageSize < 1)
            throw new IllegalArgumentException(MessageConstant.PAGE_SIZE_ARGUMENT_INVALID);
        return new PageDTO<>(list, total, total / pageSize + (total % pageSize == 0 ? 0 : 1));
    }
}
