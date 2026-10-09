package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情持久化类
 * 映射order_detail表数据
 */
@Data
@TableName(value = "order_detail", autoResultMap = true)
public class OrderDetail {

    @TableId(value = "order_id", type = IdType.INPUT)
    private Long orderId;
    @TableField(value = "category_id")
    private Long categoryId;
    @TableField(value = "item_id")
    private Long itemId;
    @TableField(value = "category_name")
    private String categoryName;
    @TableField(value = "item_name")
    private String itemName;
    @TableField(value = "sku_name")
    private String skuName;
    @TableField(value = "service_kind")
    private ServiceKind serviceKind;
    @TableField(value = "standard_price")
    private BigDecimal standardPrice;
    @TableField(value = "minimum_offer_price")
    private BigDecimal minimumOfferPrice;
    @TableField(value = "duration_minutes")
    private Integer durationMinutes;
    @TableField(value = "unit")
    private String unit;
    @TableField(value = "description")
    private String description;
    @TableField(value = "included")
    private String included;
    @TableField(value = "excluded")
    private String excluded;
    @TableField(value = "customer_supplies_parts")
    private Boolean customerSuppliesParts;
    @TableField(value = "address_contact_name")
    private String addressContactName;
    @TableField(value = "address_contact_phone")
    private String addressContactPhone;
    @TableField(value = "province_code")
    private String provinceCode;
    @TableField(value = "province_name")
    private String provinceName;
    @TableField(value = "city_code")
    private String cityCode;
    @TableField(value = "city_name")
    private String cityName;
    @TableField(value = "district_code")
    private String districtCode;
    @TableField(value = "district_name")
    private String districtName;
    @TableField(value = "detail")
    private String detail;
    @TableField(value = "longitude")
    private BigDecimal longitude;
    @TableField(value = "latitude")
    private BigDecimal latitude;
    @TableField(value = "was_default")
    private Boolean wasDefault;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
