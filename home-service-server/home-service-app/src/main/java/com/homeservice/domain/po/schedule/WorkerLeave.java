package com.homeservice.domain.po.schedule;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("worker_leave")
public class WorkerLeave {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long workerId; // 服务人员ID
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
    private String reason; // 原因
    private String status; // 状态
    private LocalDateTime cancelledAt; // 取消时间
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
