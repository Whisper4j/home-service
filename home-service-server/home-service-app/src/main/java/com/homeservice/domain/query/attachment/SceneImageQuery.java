package com.homeservice.domain.query.attachment;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class SceneImageQuery {
    @ApiId
    @Positive(message = MessageConstant.ORDER_SELECTION_INVALID)
    private Long orderId; // 订单ID

}
