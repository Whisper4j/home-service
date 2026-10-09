package com.homeservice.domain.po.dispatch;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.BookingType;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("order_assignment")
public class OrderAssignment {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private Long workerId; // 服务人员ID
    private String workerName; // 服务人员姓名
    private BookingType bookingType; // 预约类型
    private String status; // 状态
    private LocalDateTime assignedAt; // 分配时间
    private LocalDateTime releasedAt; // 释放时间
    private String releaseReason; // 释放原因
    private LocalDateTime finishedAt; // 完成时间
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
