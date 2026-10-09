package com.homeservice.handler.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;

import java.util.List;

/**
 * 休息星期处理器类
 * 转换休息星期对应的数据库字段
 */
public class RestWeekdaysTypeHandler extends TypedJsonTypeHandler<List<Integer>> {
    /**
     * 创建休息星期类型Handler实例
     */
    public RestWeekdaysTypeHandler() {
        super(new TypeReference<List<Integer>>() {});
    }
}
