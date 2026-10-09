package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.ServiceKind;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("client_entry")
public class ClientEntry {

    @TableId(type = IdType.INPUT)
    private String code; // 编码
    private ServiceKind serviceKind; // 服务类型
    private String groupCode; // 分组编码
    private String groupName; // 分组名称
    private String groupDescription; // 分组说明
    private Integer groupSort; // 分组排序
    private String name; // 名称
    private String description; // 说明
    private Integer sortNo; // 排序号
    private Boolean enabled; // 是否启用
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
