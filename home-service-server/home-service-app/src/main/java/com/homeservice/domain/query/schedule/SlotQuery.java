package com.homeservice.domain.query.schedule;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SlotQuery {

    @NotNull(message = MessageConstant.DATE_REQUIRED)
    private LocalDate date; // 日期

}
