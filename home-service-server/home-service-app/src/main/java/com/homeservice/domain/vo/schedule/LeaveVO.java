package com.homeservice.domain.vo.schedule;

import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class LeaveVO {

    @ApiId
    private Long id; // 主键ID

    private OffsetDateTime startTime; // 开始时间

    private OffsetDateTime endTime; // 结束时间

    private String reason; // 原因

    private String status; // 状态
}
