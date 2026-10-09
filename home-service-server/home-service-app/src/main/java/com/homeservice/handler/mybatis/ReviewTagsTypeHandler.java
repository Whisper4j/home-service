package com.homeservice.handler.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;

import java.util.List;

/**
 * 评价标签处理器类
 * 转换评价标签对应的数据库字段
 */
public class ReviewTagsTypeHandler extends TypedJsonTypeHandler<List<String>> {
    /**
     * 创建评价标签类型Handler实例
     */
    public ReviewTagsTypeHandler() {
        super(new TypeReference<List<String>>() {});
    }
}
