package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务规格持久化类
 * 映射service_sku表数据
 */
@Data
@TableName(value = "service_sku", autoResultMap = true)
public class ServiceSku {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "item_id")
    private Long itemId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "standard_price")
    private BigDecimal standardPrice;
    @TableField(value = "minimum_offer_price")
    private BigDecimal minimumOfferPrice;
    @TableField(value = "duration_minutes")
    private Integer durationMinutes;
    @TableField(value = "unit")
    private String unit;
    @TableField(value = "status")
    private CatalogStatus status;
    @TableField(value = "supports_offer")
    private Boolean supportsOffer;
    @TableField(value = "description")
    private String description;
    @TableField(value = "included")
    private String included;
    @TableField(value = "excluded")
    private String excluded;
    @TableField(value = "customer_supplies_parts")
    private Boolean customerSuppliesParts;
    @TableField(value = "client_entry_code")
    private String clientEntryCode;
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
