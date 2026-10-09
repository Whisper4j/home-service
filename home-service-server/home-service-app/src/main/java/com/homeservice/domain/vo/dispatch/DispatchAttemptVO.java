package com.homeservice.domain.vo.dispatch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class DispatchAttemptVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long orderId; // 订单ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long workerId; // 服务人员ID

    private String result; // 尝试结果

    private String reason; // 原因

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer serviceMinutes; // 预计服务分钟数

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderCount; // 当日订单数

    private OffsetDateTime createdAt; // 创建时间
}
