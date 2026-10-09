package com.homeservice.domain.value;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyResponse {

    private String code; // 业务码
    private String message; // 提示信息
    private JsonNode data; // 响应数据
}
