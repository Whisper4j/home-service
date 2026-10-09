package com.homeservice.common.domain;

import java.util.List;

import lombok.Data;

@Data
public class PageDTO<T> {

    private List<T> list; // 当前页数据
    private long total; // 总记录数
    private long pages; // 总页数

    public PageDTO(List<T> list, long total, long pages) {
        this.list = List.copyOf(list);
        if (total < 0 || pages < 0) throw new IllegalArgumentException("分页计数不可为负");
        this.total = total;
        this.pages = pages;
    }

    public static <T> PageDTO<T> of(List<T> list, long total, long pageSize) {
        if (pageSize < 1) throw new IllegalArgumentException("pageSize 必须为正");
        return new PageDTO<>(list, total, total / pageSize + (total % pageSize == 0 ? 0 : 1));
    }
}
