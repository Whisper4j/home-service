package com.homeservice.domain.vo.statistics;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class WorkerStatisticsVO {

    private OffsetDateTime asOf; // 统计截止时间

    private LocalDate today; // 统计日期

    private String month; // 月份

    private Integer todayPendingCount; // 今日待服务数

    private Integer monthCompletedCount; // 本月完成数

    private Integer totalCompletedCount; // 累计完成数

    private Integer monthBookedMinutes; // 本月预约分钟数

    private Integer totalBookedMinutes; // 累计预约分钟数
}
