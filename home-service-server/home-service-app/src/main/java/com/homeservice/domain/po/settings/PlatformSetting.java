package com.homeservice.domain.po.settings;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.handler.mybatis.ImageMimeTypesTypeHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
@TableName(value = "platform_setting", autoResultMap = true)
public class PlatformSetting {

    @TableId(type = IdType.INPUT)
    private Integer id; // 主键ID
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
    private BigDecimal priceStep; // 调价步长
    private Integer scheduleWindowDays; // 排班窗口天数
    private Integer leaveLeadHours; // 请假提前小时数
    private Integer sceneImageMaxCount; // 现场图片上限
    private Long sceneImageMaxBytes; // 单张图片字节上限
    @TableField(typeHandler = ImageMimeTypesTypeHandler.class)
    private List<String> sceneImageMimeTypes; // 允许的图片类型（JSON）
    private Long updatedBy; // 更新人账号ID
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
