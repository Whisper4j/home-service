package com.homeservice.domain.po.account;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务人员资料持久化类
 * 映射worker_profile表数据
 */
@Data
@TableName(value = "worker_profile", autoResultMap = true)
public class WorkerProfile {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "account_id")
    private Long accountId;
    @TableField(value = "city_code")
    private String cityCode;
    @TableField(value = "dispatch_enabled")
    private Boolean dispatchEnabled;
    @TableField(value = "work_intervals", typeHandler = WorkIntervalsTypeHandler.class)
    private List<WorkIntervalValue> workIntervals;
    @TableField(value = "rest_weekdays", typeHandler = RestWeekdaysTypeHandler.class)
    private List<Integer> restWeekdays;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
