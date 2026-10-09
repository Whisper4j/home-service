package com.homeservice.domain.vo.order;

import com.homeservice.handler.json.ApiIds;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ServiceSnapshotVO {

    private String categoryName; // 分类名称

    private String itemName; // 项目名称

    private String skuName; // 规格名称

    @ApiMoney
    private BigDecimal standardPrice; // 标准价格

    @ApiMoney
    private BigDecimal minimumOfferPrice; // 最低优惠价

    private Integer durationMinutes; // 服务时长（分钟）

    private String unit; // 计价单位

    @ApiIds
    private List<Long> skillIds; // 技能ID列表

    private String description; // 说明

    private String included; // 包含内容

    private String excluded; // 不含内容

    private Boolean customerSuppliesParts; // 是否客户自备配件
}
