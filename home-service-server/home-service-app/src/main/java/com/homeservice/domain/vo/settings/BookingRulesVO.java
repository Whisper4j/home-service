package com.homeservice.domain.vo.settings;

import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class BookingRulesVO {

    private String cityCode; // 城市编码

    private LocalTime workStart; // 每日工作开始时间

    private LocalTime workEnd; // 每日工作结束时间

    private Integer slotMinutes; // 时间槽分钟数

    private Integer earliestHours; // 最早预约小时数

    private Integer latestDays; // 最远预约天数

    private Integer offerLeadHours; // 优惠提前小时数

    private Integer offerWaitMinutes; // 抢单等待分钟数

    private Integer offerSafetyHours; // 优惠安全小时数

    private Integer paymentTimeoutMinutes; // 支付超时分钟数

    private Integer dispatchWaitMinutes; // 派单等待分钟数

    private Integer dispatchScanSeconds; // 派单扫描秒数

    private Integer standardBufferMinutes; // 标准单缓冲分钟数

    private Integer offerBufferMinutes; // 优惠单缓冲分钟数

    private Integer autoConfirmHours; // 自动确认小时数

    @ApiMoney
    private BigDecimal priceStep; // 调价步长

    private String cityName; // 城市名称

    private Integer scheduleWindowDays; // 排班窗口天数

    private Integer leaveLeadHours; // 请假提前小时数

    private Integer sceneImageMaxCount; // 现场图片上限

    private Integer sceneImageMaxBytes; // 单张图片字节上限

    private List<String> sceneImageMimeTypes; // 允许的图片类型（JSON）
}
