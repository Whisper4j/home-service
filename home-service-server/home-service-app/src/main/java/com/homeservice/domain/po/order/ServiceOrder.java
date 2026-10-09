package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.BookingType;
import com.homeservice.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@TableName("service_order")
public class ServiceOrder {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long customerId; // 客户ID
    private Long skuId; // 服务规格ID
    private Long addressId; // 地址ID
    private BookingType bookingType; // 预约类型
    private OrderStatus status; // 状态
    private String paymentStatus; // 支付状态
    private String dispatchStatus; // 派单状态
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
    private LocalDateTime bufferEndTime; // 缓冲结束时间
    private LocalDateTime paymentDeadline; // 支付截止时间
    private LocalDateTime offerDeadline; // 抢单截止时间
    private LocalDateTime dispatchDeadline; // 派单截止时间
    private LocalDateTime confirmationDeadline; // 确认截止时间
    private LocalDateTime offerPublishedAt; // 优惠发布时间
    private LocalDateTime nextDispatchAt; // 下次派单时间
    private Long dispatchAttemptCount; // 派单尝试次数
    private String dispatchFailureReason; // 派单失败原因
    private BigDecimal currentPrice; // 当前价格
    private BigDecimal dealPrice; // 成交价格
    private Integer priceVersion; // 价格版本
    private Integer stateVersion; // 状态版本
    private String contactName; // 联系人
    private String contactPhone; // 联系电话
    @ToString.Exclude
    private String startCode; // 服务开始码（日志隐藏）
    private String remark; // 备注
    private String cancellationReason; // 取消原因
    private LocalDateTime closedAt; // 关闭时间
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
