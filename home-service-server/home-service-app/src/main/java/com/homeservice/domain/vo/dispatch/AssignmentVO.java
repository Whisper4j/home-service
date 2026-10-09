package com.homeservice.domain.vo.dispatch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.BookingType;
import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class AssignmentVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long orderId; // 订单ID

    @ApiId
    private Long workerId; // 服务人员ID

    private String workerName; // 服务人员姓名

    private BookingType bookingType; // 预约类型

    private String status; // 状态

    private OffsetDateTime assignedAt; // 分配时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime releasedAt; // 释放时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String releaseReason; // 释放原因

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime finishedAt; // 完成时间
}
