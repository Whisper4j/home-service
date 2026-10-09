package com.homeservice.domain.po.dispatch;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("dispatch_attempt")
public class DispatchAttempt {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private Long workerId; // 服务人员ID
    private String result; // 尝试结果
    private String reason; // 原因
    private Long serviceMinutes; // 预计服务分钟数
    private Long orderCount; // 当日订单数
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
