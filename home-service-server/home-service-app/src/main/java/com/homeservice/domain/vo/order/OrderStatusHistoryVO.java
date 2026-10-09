package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.OrderStatus;
import com.homeservice.enums.Role;
import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class OrderStatusHistoryVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long orderId; // 订单ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus fromStatus; // 原订单状态

    private OrderStatus toStatus; // 新订单状态

    private String actorType; // 操作者类型

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long actorId; // 操作者账号ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role actorRole; // 操作者角色

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reason; // 原因

    private OffsetDateTime createdAt; // 创建时间
}
