package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务订单持久化类
 * 映射service_order表数据
 */
@Data
@TableName(value = "service_order", autoResultMap = true)
public class ServiceOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "customer_id")
    private Long customerId;
    @TableField(value = "sku_id")
    private Long skuId;
    @TableField(value = "address_id")
    private Long addressId;
    @TableField(value = "booking_type")
    private BookingType bookingType;
    @TableField(value = "status")
    private OrderStatus status;
    @TableField(value = "payment_status")
    private PaymentStatus paymentStatus;
    @TableField(value = "dispatch_status")
    private DispatchStatus dispatchStatus;
    @TableField(value = "start_time")
    private LocalDateTime startTime;
    @TableField(value = "end_time")
    private LocalDateTime endTime;
    @TableField(value = "buffer_end_time")
    private LocalDateTime bufferEndTime;
    @TableField(value = "payment_deadline")
    private LocalDateTime paymentDeadline;
    @TableField(value = "offer_deadline")
    private LocalDateTime offerDeadline;
    @TableField(value = "dispatch_deadline")
    private LocalDateTime dispatchDeadline;
    @TableField(value = "confirmation_deadline")
    private LocalDateTime confirmationDeadline;
    @TableField(value = "offer_published_at")
    private LocalDateTime offerPublishedAt;
    @TableField(value = "next_dispatch_at")
    private LocalDateTime nextDispatchAt;
    @TableField(value = "dispatch_attempt_count")
    private Long dispatchAttemptCount;
    @TableField(value = "dispatch_failure_reason")
    private String dispatchFailureReason;
    @TableField(value = "current_price")
    private BigDecimal currentPrice;
    @TableField(value = "deal_price")
    private BigDecimal dealPrice;
    @TableField(value = "price_version")
    private Integer priceVersion;
    @TableField(value = "state_version")
    private Integer stateVersion;
    @TableField(value = "contact_name")
    private String contactName;
    @TableField(value = "contact_phone")
    private String contactPhone;
    @TableField(value = "start_code")
    @ToString.Exclude
    private String startCode;
    @TableField(value = "remark")
    private String remark;
    @TableField(value = "cancellation_reason")
    private String cancellationReason;
    @TableField(value = "closed_at")
    private LocalDateTime closedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
