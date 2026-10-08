package com.homeservice.domain.po.settings;
import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.domain.value.*;
import com.homeservice.handler.mybatis.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/** platform_setting 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "platform_setting", autoResultMap = true)
public class PlatformSetting {
    /** 固定单例ID=1 */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /** 当前服务城市代码 */
    @TableField(value = "city_code")
    private String cityCode;

    /** 平台工作开始时间 */
    @TableField(value = "work_start")
    private LocalTime workStart;

    /** 平台工作结束时间 */
    @TableField(value = "work_end")
    private LocalTime workEnd;

    /** 时间槽分钟数 */
    @TableField(value = "slot_minutes")
    private Integer slotMinutes;

    /** 最早预约提前小时数，可维护 */
    @TableField(value = "earliest_hours")
    private Integer earliestHours;

    /** 最远预约天数，可维护 */
    @TableField(value = "latest_days")
    private Integer latestDays;

    /** 优惠预约最少提前小时数 */
    @TableField(value = "offer_lead_hours")
    private Integer offerLeadHours;

    /** 优惠最长等待分钟数 */
    @TableField(value = "offer_wait_minutes")
    private Integer offerWaitMinutes;

    /** 服务前安全截止小时数 */
    @TableField(value = "offer_safety_hours")
    private Integer offerSafetyHours;

    /** 待支付超时分钟数 */
    @TableField(value = "payment_timeout_minutes")
    private Integer paymentTimeoutMinutes;

    /** 标准派单最长等待分钟数 */
    @TableField(value = "dispatch_wait_minutes")
    private Integer dispatchWaitMinutes;

    /** 派单扫描间隔秒数 */
    @TableField(value = "dispatch_scan_seconds")
    private Integer dispatchScanSeconds;

    /** 标准订单尾部缓冲分钟数 */
    @TableField(value = "standard_buffer_minutes")
    private Integer standardBufferMinutes;

    /** 优惠订单尾部缓冲分钟数 */
    @TableField(value = "offer_buffer_minutes")
    private Integer offerBufferMinutes;

    /** 自动完成确认小时数 */
    @TableField(value = "auto_confirm_hours")
    private Integer autoConfirmHours;

    /** 优惠报价步长 */
    @TableField(value = "price_step")
    private BigDecimal priceStep;

    /** 时间槽滚动窗口天数 */
    @TableField(value = "schedule_window_days")
    private Integer scheduleWindowDays;

    /** 请假最少提前小时数 */
    @TableField(value = "leave_lead_hours")
    private Integer leaveLeadHours;

    /** 每单现场图片上限 */
    @TableField(value = "scene_image_max_count")
    private Integer sceneImageMaxCount;

    /** 单图字节上限 */
    @TableField(value = "scene_image_max_bytes")
    private Long sceneImageMaxBytes;

    /** 允许上传的图片MIME数组 */
    @TableField(value = "scene_image_mime_types", typeHandler = ImageMimeTypesTypeHandler.class)
    private List<ImageMimeType> sceneImageMimeTypes;

    /** 最后修改管理员账号ID；初始化时为空 */
    @TableField(value = "updated_by")
    private Long updatedBy;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
