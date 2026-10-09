package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.CatalogStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("service_sku")
public class ServiceSku {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long itemId; // 服务项目ID
    private String name; // 名称
    private BigDecimal standardPrice; // 标准价格
    private BigDecimal minimumOfferPrice; // 最低优惠价
    private Integer durationMinutes; // 服务时长（分钟）
    private String unit; // 计价单位
    private CatalogStatus status; // 状态
    private Boolean supportsOffer; // 是否支持优惠
    private String description; // 说明
    private String included; // 包含内容
    private String excluded; // 不含内容
    private Boolean customerSuppliesParts; // 是否客户自备配件
    private String clientEntryCode; // 客户端入口编码
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt; // 删除时间（NULL有效）
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
