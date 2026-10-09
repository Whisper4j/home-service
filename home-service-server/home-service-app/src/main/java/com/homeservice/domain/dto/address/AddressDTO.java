package com.homeservice.domain.dto.address;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AddressDTO {

    @Size(max = 40, message = MessageConstant.CONTACT_NAME_TOO_LONG)
    @NotBlank(message = MessageConstant.CONTACT_NAME_REQUIRED)
    private String contactName; // 联系人

    @Size(max = 11, message = MessageConstant.CONTACT_PHONE_INVALID)
    @NotBlank(message = MessageConstant.CONTACT_PHONE_REQUIRED)
    @Pattern(regexp = "^1[0-9]{10}$", message = MessageConstant.CONTACT_PHONE_INVALID)
    private String contactPhone; // 联系电话

    @Size(max = 6, message = MessageConstant.ADDRESS_REGION_INVALID)
    @NotBlank(message = MessageConstant.ADDRESS_REGION_REQUIRED)
    private String provinceCode; // 省编码

    @Size(max = 40, message = MessageConstant.ADDRESS_REGION_INVALID)
    @NotBlank(message = MessageConstant.ADDRESS_REGION_REQUIRED)
    private String provinceName; // 省名称

    @Size(max = 6, message = MessageConstant.CITY_INVALID)
    @NotBlank(message = MessageConstant.CITY_REQUIRED)
    private String cityCode; // 城市编码

    @Size(max = 40, message = MessageConstant.ADDRESS_REGION_INVALID)
    @NotBlank(message = MessageConstant.ADDRESS_REGION_REQUIRED)
    private String cityName; // 城市名称

    @Size(max = 6, message = MessageConstant.ADDRESS_REGION_INVALID)
    @NotBlank(message = MessageConstant.ADDRESS_REGION_REQUIRED)
    private String districtCode; // 区县编码

    @Size(max = 40, message = MessageConstant.ADDRESS_REGION_INVALID)
    @NotBlank(message = MessageConstant.ADDRESS_REGION_REQUIRED)
    private String districtName; // 区县名称

    @Size(max = 200, message = MessageConstant.ADDRESS_DETAIL_TOO_LONG)
    @NotBlank(message = MessageConstant.ADDRESS_DETAIL_REQUIRED)
    private String detail; // 详细地址

    @NotNull(message = MessageConstant.ADDRESS_DEFAULT_REQUIRED)
    private Boolean isDefault; // 是否默认地址
}
