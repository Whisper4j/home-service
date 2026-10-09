package com.homeservice.domain.vo.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.ServiceKind;

import lombok.Data;

@Data
public class ClientEntryVO {

    private String code; // 编码

    private ServiceKind serviceKind; // 服务类型

    private String groupCode; // 分组编码

    private String groupName; // 分组名称

    private String groupDescription; // 分组说明

    private Integer groupSort; // 分组排序

    private String name; // 名称

    private String description; // 说明

    private Integer sort; // 排序

    private Boolean available; // 是否可用

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unavailableReason; // 不可用原因

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SkuVO sku; // 规格
}
