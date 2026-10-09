package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务分类持久化类
 * 映射service_category表数据
 */
@Data
@TableName(value = "service_category", autoResultMap = true)
public class ServiceCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "sort_no")
    private Integer sortNo;
    @TableField(value = "status")
    private CatalogStatus status;
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
