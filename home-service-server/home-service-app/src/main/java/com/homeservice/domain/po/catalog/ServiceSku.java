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
/** service_sku 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "service_sku", autoResultMap = true)
public class ServiceSku {
    /** 服务规格SKU ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 服务项目ID */
    @TableField(value = "item_id")
    private Long itemId;

    /** 规格名称 */
    @TableField(value = "name")
    private String name;

    /** 标准价，范围与OpenAPI Money一致 */
    @TableField(value = "standard_price")
    private BigDecimal standardPrice;

    /** 最低优惠报价；不支持优惠时等于标准价 */
    @TableField(value = "minimum_offer_price")
    private BigDecimal minimumOfferPrice;

    /** 预计服务分钟数，30分钟整数倍 */
    @TableField(value = "duration_minutes")
    private Integer durationMinutes;

    /** 计价单位展示文案 */
    @TableField(value = "unit")
    private String unit;

    /** 状态：ON_SHELF/OFF_SHELF */
    @TableField(value = "status")
    private CatalogStatus status;

    /** 是否支持优惠预约 */
    @TableField(value = "supports_offer")
    private Boolean supportsOffer;

    /** 服务说明 */
    @TableField(value = "description")
    private String description;

    /** 包含内容 */
    @TableField(value = "included")
    private String included;

    /** 不包含内容 */
    @TableField(value = "excluded")
    private String excluded;

    /** 是否由客户自备配件 */
    @TableField(value = "customer_supplies_parts")
    private Boolean customerSuppliesParts;

    /** 绑定的客户端入口编码 */
    @TableField(value = "client_entry_code")
    private String clientEntryCode;

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
