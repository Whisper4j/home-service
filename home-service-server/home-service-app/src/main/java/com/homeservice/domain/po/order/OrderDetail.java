package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.ServiceKind;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("order_detail")
public class OrderDetail {

    @TableId(value = "order_id", type = IdType.INPUT)
    private Long orderId; // 订单ID
    private Long categoryId; // 分类ID
    private Long itemId; // 服务项目ID
    private String categoryName; // 分类名称
    private String itemName; // 项目名称
    private String skuName; // 规格名称
    private ServiceKind serviceKind; // 服务类型
    private BigDecimal standardPrice; // 标准价格
    private BigDecimal minimumOfferPrice; // 最低优惠价
    private Integer durationMinutes; // 服务时长（分钟）
    private String unit; // 计价单位
    private String description; // 说明
    private String included; // 包含内容
    private String excluded; // 不含内容
    private Boolean customerSuppliesParts; // 是否客户自备配件
    private String addressContactName; // 地址联系人
    private String addressContactPhone; // 地址联系电话
    private String provinceCode; // 省编码
    private String provinceName; // 省名称
    private String cityCode; // 城市编码
    private String cityName; // 城市名称
    private String districtCode; // 区县编码
    private String districtName; // 区县名称
    private String detail; // 详细地址
    private BigDecimal longitude; // 经度
    private BigDecimal latitude; // 纬度
    private Boolean wasDefault; // 下单时是否默认地址
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
