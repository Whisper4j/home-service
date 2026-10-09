package com.homeservice.domain.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AddressDTO {

    @Size(max = 40, message = "联系人长度不能超过40")
    @NotBlank(message = "联系人不能为空")
    private String contactName; // 联系人

    @Size(max = 11, message = "联系电话长度不能超过11")
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "联系电话格式不正确")
    private String contactPhone; // 联系电话

    @Size(max = 6, message = "省编码长度不能超过6")
    @NotBlank(message = "省编码不能为空")
    private String provinceCode; // 省编码

    @Size(max = 40, message = "省名称长度不能超过40")
    @NotBlank(message = "省名称不能为空")
    private String provinceName; // 省名称

    @Size(max = 6, message = "城市编码长度不能超过6")
    @NotBlank(message = "城市编码不能为空")
    private String cityCode; // 城市编码

    @Size(max = 40, message = "城市名称长度不能超过40")
    @NotBlank(message = "城市名称不能为空")
    private String cityName; // 城市名称

    @Size(max = 6, message = "区县编码长度不能超过6")
    @NotBlank(message = "区县编码不能为空")
    private String districtCode; // 区县编码

    @Size(max = 40, message = "区县名称长度不能超过40")
    @NotBlank(message = "区县名称不能为空")
    private String districtName; // 区县名称

    @Size(max = 200, message = "详细地址长度不能超过200")
    @NotBlank(message = "详细地址不能为空")
    private String detail; // 详细地址

    @NotNull(message = "是否默认地址不能为空")
    private Boolean isDefault; // 是否默认地址
}
