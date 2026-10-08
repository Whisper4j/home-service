package com.homeservice.domain.value;
import com.fasterxml.jackson.databind.JsonNode;
/** 统一响应快照。data 对应不同接口的异构结构，保存树而非丢失类型的字符串/Map；不含凭证。 */
public record IdempotencyResponse(String code, String message, JsonNode data) {}
