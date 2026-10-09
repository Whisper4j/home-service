package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户端入口持久化类
 * 映射client_entry表数据
 */
@Data
@TableName(value = "client_entry", autoResultMap = true)
public class ClientEntry {

    @TableId(value = "code", type = IdType.INPUT)
    private String code;
    @TableField(value = "service_kind")
    private ServiceKind serviceKind;
    @TableField(value = "group_code")
    private String groupCode;
    @TableField(value = "group_name")
    private String groupName;
    @TableField(value = "group_description")
    private String groupDescription;
    @TableField(value = "group_sort")
    private Integer groupSort;
    @TableField(value = "name")
    private String name;
    @TableField(value = "description")
    private String description;
    @TableField(value = "sort_no")
    private Integer sortNo;
    @TableField(value = "enabled")
    private Boolean enabled;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
