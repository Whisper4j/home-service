package com.homeservice.handler.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;

/**
 * 幂等响应处理器类
 * 转换幂等响应对应的数据库字段
 */
public class IdempotencyResponseTypeHandler extends TypedJsonTypeHandler<IdempotencyResponse> {
    /**
     * 创建幂等响应类型Handler实例
     */
    public IdempotencyResponseTypeHandler() {
        super(new TypeReference<IdempotencyResponse>() {});
    }
}
