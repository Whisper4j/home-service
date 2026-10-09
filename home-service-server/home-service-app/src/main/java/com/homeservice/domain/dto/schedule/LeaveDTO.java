package com.homeservice.domain.dto.schedule;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class LeaveDTO {

    @NotNull(message = MessageConstant.START_TIME_REQUIRED)
    private OffsetDateTime startTime; // 开始时间

    @NotNull(message = MessageConstant.END_TIME_REQUIRED)
    private OffsetDateTime endTime; // 结束时间

    @Size(max = 300, message = MessageConstant.LEAVE_REASON_TOO_LONG)
    @NotBlank(message = MessageConstant.LEAVE_REASON_REQUIRED)
    private String reason; // 原因

    @AssertTrue(message = MessageConstant.LEAVE_TIME_INVALID)
    @JsonIgnore
    public boolean isValidRange() {
        return startTime == null
                || endTime == null
                || startTime.isBefore(endTime)
                        && startTime.getMinute() % 30 == 0
                        && endTime.getMinute() % 30 == 0
                        && startTime.getSecond() == 0
                        && endTime.getSecond() == 0;
    }
}
