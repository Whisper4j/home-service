package com.homeservice.handler.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;

import java.util.List;

/**
 * 工作时段集合处理器类
 * 转换工作时段集合对应的数据库字段
 */
public class WorkIntervalsTypeHandler extends TypedJsonTypeHandler<List<WorkIntervalValue>> {
    /**
     * 创建工作时段集合类型Handler实例
     */
    public WorkIntervalsTypeHandler() {
        super(new TypeReference<List<WorkIntervalValue>>() {});
    }
}
