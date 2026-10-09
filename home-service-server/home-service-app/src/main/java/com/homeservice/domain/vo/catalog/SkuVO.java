package com.homeservice.domain.vo.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.CatalogStatus;
import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiIds;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SkuVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long categoryId; // 分类ID

    private String categoryName; // 分类名称

    private String itemName; // 项目名称

    @ApiId
    private Long itemId; // 服务项目ID

    private String name; // 名称

    @ApiMoney
    private BigDecimal standardPrice; // 标准价格

    @ApiMoney
    private BigDecimal minimumOfferPrice; // 最低优惠价

    private Integer durationMinutes; // 服务时长（分钟）

    private String unit; // 计价单位

    @ApiIds
    private List<Long> skillIds; // 技能ID列表

    private CatalogStatus status; // 状态

    private Boolean supportsOffer; // 是否支持优惠

    private String description; // 说明

    private String included; // 包含内容

    private String excluded; // 不含内容

    private Boolean customerSuppliesParts; // 是否客户自备配件

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientEntryCode; // 客户端入口编码
}
