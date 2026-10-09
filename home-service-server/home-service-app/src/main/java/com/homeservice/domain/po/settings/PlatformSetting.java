package com.homeservice.domain.po.settings;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 平台设置持久化类
 * 映射platform_setting表数据
 */
@Data
@TableName(value = "platform_setting", autoResultMap = true)
public class PlatformSetting {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    @TableField(value = "city_code")
    private String cityCode;
    @TableField(value = "work_start")
    private LocalTime workStart;
    @TableField(value = "work_end")
    private LocalTime workEnd;
    @TableField(value = "slot_minutes")
    private Integer slotMinutes;
    @TableField(value = "earliest_hours")
    private Integer earliestHours;
    @TableField(value = "latest_days")
    private Integer latestDays;
    @TableField(value = "offer_lead_hours")
    private Integer offerLeadHours;
    @TableField(value = "offer_wait_minutes")
    private Integer offerWaitMinutes;
    @TableField(value = "offer_safety_hours")
    private Integer offerSafetyHours;
    @TableField(value = "payment_timeout_minutes")
    private Integer paymentTimeoutMinutes;
    @TableField(value = "dispatch_wait_minutes")
    private Integer dispatchWaitMinutes;
    @TableField(value = "dispatch_scan_seconds")
    private Integer dispatchScanSeconds;
    @TableField(value = "standard_buffer_minutes")
    private Integer standardBufferMinutes;
    @TableField(value = "offer_buffer_minutes")
    private Integer offerBufferMinutes;
    @TableField(value = "auto_confirm_hours")
    private Integer autoConfirmHours;
    @TableField(value = "price_step")
    private BigDecimal priceStep;
    @TableField(value = "schedule_window_days")
    private Integer scheduleWindowDays;
    @TableField(value = "leave_lead_hours")
    private Integer leaveLeadHours;
    @TableField(value = "scene_image_max_count")
    private Integer sceneImageMaxCount;
    @TableField(value = "scene_image_max_bytes")
    private Long sceneImageMaxBytes;
    @TableField(value = "scene_image_mime_types", typeHandler = ImageMimeTypesTypeHandler.class)
    private List<ImageMimeType> sceneImageMimeTypes;
    @TableField(value = "updated_by")
    private Long updatedBy;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
