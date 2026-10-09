package com.homeservice.domain.query.attachment;

import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class OrderSceneImageQuery {

    @NotNull(message = "订单ID不能为空")
    @ApiId
    @Positive(message = "订单ID必须大于0")
    private Long orderId; // 订单ID

}
