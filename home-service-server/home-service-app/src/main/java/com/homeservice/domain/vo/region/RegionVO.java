package com.homeservice.domain.vo.region;

import java.util.List;

import lombok.Data;

@Data
public class RegionVO {

    private String provinceCode; // 省编码

    private String provinceName; // 省名称

    private String cityCode; // 城市编码

    private String cityName; // 城市名称

    private List<DistrictVO> districts; // 区县列表
}
