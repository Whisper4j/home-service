package com.homeservice.domain.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class LeaveDTO {

    @NotNull(message = "开始时间不能为空")
    private OffsetDateTime startTime; // 开始时间

    @NotNull(message = "结束时间不能为空")
    private OffsetDateTime endTime; // 结束时间

    @Size(max = 300, message = "原因长度不能超过300")
    @NotBlank(message = "原因不能为空")
    private String reason; // 原因

    @AssertTrue(message = "请假起止时间须按半小时对齐且开始早于结束")
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
