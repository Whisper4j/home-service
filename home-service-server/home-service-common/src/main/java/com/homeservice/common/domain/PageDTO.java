package com.homeservice.common.domain;

import java.util.List;

/**
 * 分页响应类
 * 封装分页列表、总数和总页数
 */
public record PageDTO<T>(List<T> list, long total, long pages) {
    /**
     * 创建并校验分页请求实例
     */
    public PageDTO {
        list = List.copyOf(list);
        if (total < 0 || pages < 0) throw new IllegalArgumentException("分页计数不可为负");
    }

    /**
     * 创建分页响应对象
     */
    public static <T> PageDTO<T> of(List<T> list, long total, long pageSize) {
        if (pageSize < 1) throw new IllegalArgumentException("pageSize 必须为正");
        return new PageDTO<>(list, total, total / pageSize + (total % pageSize == 0 ? 0 : 1));
    }
}
