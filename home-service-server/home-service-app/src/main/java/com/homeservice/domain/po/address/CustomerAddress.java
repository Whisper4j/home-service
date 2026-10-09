package com.homeservice.domain.po.address;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户地址持久化类
 * 映射customer_address表数据
 */
@Data
@TableName(value = "customer_address", autoResultMap = true)
public class CustomerAddress {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "customer_id")
    private Long customerId;
    @TableField(value = "contact_name")
    private String contactName;
    @TableField(value = "contact_phone")
    private String contactPhone;
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
    @TableField(value = "is_default")
    private Boolean isDefault;
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
