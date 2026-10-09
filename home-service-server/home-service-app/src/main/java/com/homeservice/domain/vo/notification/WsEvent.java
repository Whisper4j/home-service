package com.homeservice.domain.vo.notification;

import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class WsEvent {

    @ApiId
    private Long eventId; // 事件ID

    private String type; // 类型

    @ApiId
    private Long orderId; // 订单ID

    private Integer priceVersion; // 价格版本

    private OffsetDateTime occurredAt; // 发生时间

    private WsOrderPayload payload; // 事件数据
}
