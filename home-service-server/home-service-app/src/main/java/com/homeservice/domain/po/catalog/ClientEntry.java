package com.homeservice.domain.po.catalog;
import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.domain.value.*;
import com.homeservice.handler.mybatis.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/** client_entry 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "client_entry", autoResultMap = true)
public class ClientEntry {
    /** 客户端稳定入口编码 */
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    /** 服务类型：CLEANING/REPAIR/OTHER */
    @TableField(value = "service_kind")
    private ServiceKind serviceKind;

    /** 入口分组编码 */
    @TableField(value = "group_code")
    private String groupCode;

    /** 分组名称 */
    @TableField(value = "group_name")
    private String groupName;

    /** 分组说明 */
    @TableField(value = "group_description")
    private String groupDescription;

    /** 分组排序 */
    @TableField(value = "group_sort")
    private Integer groupSort;

    /** 入口名称 */
    @TableField(value = "name")
    private String name;

    /** 入口说明 */
    @TableField(value = "description")
    private String description;

    /** 组内排序 */
    @TableField(value = "sort_no")
    private Integer sortNo;

    /** 入口是否启用展示 */
    @TableField(value = "enabled")
    private Boolean enabled;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
