package com.homeservice.domain.po.account;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.domain.value.WorkIntervalValue;
import com.homeservice.handler.mybatis.RestWeekdaysTypeHandler;
import com.homeservice.handler.mybatis.WorkIntervalsTypeHandler;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
@TableName(value = "worker_profile", autoResultMap = true)
public class WorkerProfile {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long accountId; // 账号ID
    private String cityCode; // 城市编码
    private Boolean dispatchEnabled; // 是否参与派单
    @TableField(typeHandler = WorkIntervalsTypeHandler.class)
    private List<WorkIntervalValue> workIntervals; // 工作时段（JSON）
    @TableField(typeHandler = RestWeekdaysTypeHandler.class)
    private List<Integer> restWeekdays; // 休息星期（JSON）
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
