package com.homeservice.domain.value;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 幂等响应值对象类
 * 表达幂等响应相关业务值
 */
public record IdempotencyResponse(String code, String message, JsonNode data) {}
