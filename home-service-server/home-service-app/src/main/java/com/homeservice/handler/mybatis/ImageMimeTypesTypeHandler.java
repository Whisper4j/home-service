package com.homeservice.handler.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;

import java.util.List;

/**
 * 图片MIME类型集合处理器类
 * 转换图片MIME类型集合对应的数据库字段
 */
public class ImageMimeTypesTypeHandler extends TypedJsonTypeHandler<List<String>> {
    /**
     * 创建图片MIME类型集合类型Handler实例
     */
    public ImageMimeTypesTypeHandler() {
        super(new TypeReference<List<String>>() {});
    }
}
