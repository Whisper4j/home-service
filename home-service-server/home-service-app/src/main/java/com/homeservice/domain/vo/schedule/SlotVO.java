package com.homeservice.domain.vo.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.BookingType;
import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class SlotVO {

    private OffsetDateTime startTime; // 开始时间

    private OffsetDateTime endTime; // 结束时间

    private String status; // 状态

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BookingType bookingType; // 预约类型

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long assignmentId; // 分配记录ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long orderId; // 订单ID
}
