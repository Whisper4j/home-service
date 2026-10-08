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
/** service_item 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "service_item", autoResultMap = true)
public class ServiceItem {
    /** 服务项目ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 服务分类ID */
    @TableField(value = "category_id")
    private Long categoryId;

    /** 项目名称 */
    @TableField(value = "name")
    private String name;

    /** 服务类型：CLEANING/REPAIR/OTHER */
    @TableField(value = "service_kind")
    private ServiceKind serviceKind;

    /** 项目说明 */
    @TableField(value = "description")
    private String description;

    /** 状态：ON_SHELF/OFF_SHELF */
    @TableField(value = "status")
    private CatalogStatus status;

    /** 逻辑删除时间 */
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
