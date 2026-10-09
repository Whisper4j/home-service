package com.homeservice.domain.vo.order;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderAddressSnapshotVO {

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
}
