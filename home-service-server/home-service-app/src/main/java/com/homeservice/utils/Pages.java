package com.homeservice.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeservice.common.domain.*;

import java.util.function.Function;

/**
 * 分页类
 * 转换MyBatis-Plus分页请求和响应
 */
public final class Pages {
    /**
     * 创建分页实例
     */
    private Pages() {
    }

    /**
     * 创建分页查询对象
     */
    public static <T> Page<T> request(PageQuery query) {
        return new Page<>(query.getPageNo(), query.getPageSize());
    }

    /**
     * 创建分页响应对象
     */
    public static <P, V> PageDTO<V> response(IPage<P> page, Function<P, V> convert) {
        return new PageDTO<>(
                page.getRecords().stream().map(convert).toList(), page.getTotal(), page.getPages());
    }
}
