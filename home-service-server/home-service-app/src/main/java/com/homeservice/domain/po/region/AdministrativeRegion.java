package com.homeservice.domain.po.region;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 行政地区持久化类
 * 映射administrative_region表数据
 */
@Data
@TableName(value = "administrative_region", autoResultMap = true)
public class AdministrativeRegion {

    @TableId(value = "code", type = IdType.INPUT)
    private String code;
    @TableField(value = "parent_code")
    private String parentCode;
    @TableField(value = "name")
    private String name;
    @TableField(value = "level")
    private RegionLevel level;
    @TableField(value = "service_enabled")
    private Boolean serviceEnabled;
    @TableField(value = "sort_no")
    private Integer sortNo;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
