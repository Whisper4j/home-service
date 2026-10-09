package com.homeservice.domain.po.address;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("customer_address")
public class CustomerAddress {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long customerId; // 客户ID
    private String contactName; // 联系人
    private String contactPhone; // 联系电话
    private String provinceCode; // 省编码
    private String provinceName; // 省名称
    private String cityCode; // 城市编码
    private String cityName; // 城市名称
    private String districtCode; // 区县编码
    private String districtName; // 区县名称
    private String detail; // 详细地址
    private BigDecimal longitude; // 经度
    private BigDecimal latitude; // 纬度
    private Boolean isDefault; // 是否默认地址
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt; // 删除时间（NULL有效）
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
