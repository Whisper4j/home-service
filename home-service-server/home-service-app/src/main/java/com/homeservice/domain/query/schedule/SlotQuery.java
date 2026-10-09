package com.homeservice.domain.query.schedule;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SlotQuery {

    @NotNull(message = "日期不能为空")
    private LocalDate date; // 日期

}
