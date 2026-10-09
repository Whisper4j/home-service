package com.homeservice.domain.vo.notification;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class WsAuthAck {

    private String type; // 类型

    private OffsetDateTime occurredAt; // 发生时间
}
