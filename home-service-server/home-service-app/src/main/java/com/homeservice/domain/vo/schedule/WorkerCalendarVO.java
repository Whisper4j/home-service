package com.homeservice.domain.vo.schedule;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class WorkerCalendarVO {

    private LocalDate from; // 开始日期

    private LocalDate to; // 结束日期

    private ScheduleVO schedule; // 排班设置

    private List<CalendarDayVO> days; // 天数
}
