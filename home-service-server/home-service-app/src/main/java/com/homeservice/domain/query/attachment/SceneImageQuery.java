package com.homeservice.domain.query.attachment;

import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class SceneImageQuery {
    @ApiId
    @Positive(message = "订单ID必须大于0")
    private Long orderId; // 订单ID

}
