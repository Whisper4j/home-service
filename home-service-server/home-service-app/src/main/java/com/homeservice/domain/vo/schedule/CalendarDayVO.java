package com.homeservice.domain.vo.schedule;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CalendarDayVO {

    private LocalDate date; // 日期

    private String status; // 状态

    private List<SlotVO> segments; // 日历时段列表
}
