package com.homeservice.domain.po.order;
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
/** service_order 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "service_order", autoResultMap = true)
public class ServiceOrder {
    /** 订单ID，对外按十进制字符串传输 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 下单客户业务ID */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 下单SKU ID；每单一个SKU */
    @TableField(value = "sku_id")
    private Long skuId;

    /** 来源地址簿ID；展示使用独立快照 */
    @TableField(value = "address_id")
    private Long addressId;

    /** 预约类型：STANDARD/OFFER */
    @TableField(value = "booking_type")
    private BookingType bookingType;

    /** 订单履约主状态 */
    @TableField(value = "status")
    private OrderStatus status;

    /** 支付状态：UNPAID/PAID/PARTIALLY_REFUNDED/REFUNDED */
    @TableField(value = "payment_status")
    private PaymentStatus paymentStatus;

    /** 派单状态：NOT_REQUIRED/PENDING/SUCCEEDED/FAILED */
    @TableField(value = "dispatch_status")
    private DispatchStatus dispatchStatus;

    /** 预约开始时间（Asia/Shanghai） */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /** 预约服务结束时间（Asia/Shanghai） */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /** 尾部缓冲结束时间（Asia/Shanghai） */
    @TableField(value = "buffer_end_time")
    private LocalDateTime bufferEndTime;

    /** 支付截止时间（Asia/Shanghai） */
    @TableField(value = "payment_deadline")
    private LocalDateTime paymentDeadline;

    /** 优惠抢单截止时间（Asia/Shanghai） */
    @TableField(value = "offer_deadline")
    private LocalDateTime offerDeadline;

    /** 标准派单截止时间（Asia/Shanghai） */
    @TableField(value = "dispatch_deadline")
    private LocalDateTime dispatchDeadline;

    /** 客户确认截止时间（Asia/Shanghai） */
    @TableField(value = "confirmation_deadline")
    private LocalDateTime confirmationDeadline;

    /** 优惠订单首次正式进入抢单池时间 */
    @TableField(value = "offer_published_at")
    private LocalDateTime offerPublishedAt;

    /** 标准派单下次可重试时间；非待派单时为空 */
    @TableField(value = "next_dispatch_at")
    private LocalDateTime nextDispatchAt;

    /** 已执行标准派单轮数 */
    @TableField(value = "dispatch_attempt_count")
    private Long dispatchAttemptCount;

    /** 标准派单最终失败原因 */
    @TableField(value = "dispatch_failure_reason")
    private String dispatchFailureReason;

    /** 当前客户报价或标准价 */
    @TableField(value = "current_price")
    private BigDecimal currentPrice;

    /** 人员落实后的成交价 */
    @TableField(value = "deal_price")
    private BigDecimal dealPrice;

    /** 报价版本，从1开始 */
    @TableField(value = "price_version")
    private Integer priceVersion;

    /** 通用条件更新版本 */
    @TableField(value = "state_version")
    private Integer stateVersion;

    /** 本次订单联系人，不随资料或地址簿变化 */
    @TableField(value = "contact_name")
    private String contactName;

    /** 本次订单联系电话 */
    @TableField(value = "contact_phone")
    private String contactPhone;

    /** 六位开始码，仅客户鉴权接口可返回 */
    @TableField(value = "start_code")
    @ToString.Exclude
    private String startCode;

    /** 客户备注 */
    @TableField(value = "remark")
    private String remark;

    /** 取消原因 */
    @TableField(value = "cancellation_reason")
    private String cancellationReason;

    /** 完成确认或取消的实际时间，用于统计和历史排序 */
    @TableField(value = "closed_at")
    private LocalDateTime closedAt;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
