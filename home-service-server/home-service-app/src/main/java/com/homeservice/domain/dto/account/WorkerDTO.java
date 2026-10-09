package com.homeservice.domain.dto.account;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.handler.json.ApiIds;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;

import lombok.Data;

import org.hibernate.validator.constraints.UniqueElements;

@Data
public class WorkerDTO {

    @Size(max = 40, message = MessageConstant.DISPLAY_NAME_TOO_LONG)
    @NotBlank(message = MessageConstant.DISPLAY_NAME_REQUIRED)
    private String displayName; // 显示名称

    @Size(max = 11, message = MessageConstant.PHONE_INVALID)
    @NotBlank(message = MessageConstant.PHONE_REQUIRED)
    @Pattern(regexp = "^1[0-9]{10}$", message = MessageConstant.PHONE_INVALID)
    private String phone; // 手机号

    @Size(max = 6, message = MessageConstant.CITY_INVALID)
    @NotBlank(message = MessageConstant.CITY_REQUIRED)
    private String cityCode; // 城市编码

    @NotNull(message = MessageConstant.SKILL_REQUIRED)
    @Size(min = 1, message = MessageConstant.SKILL_AT_LEAST_ONE)
    @UniqueElements(message = MessageConstant.SKILL_DUPLICATED)
    @ApiIds
    @Valid
    private List<@NotNull(message = MessageConstant.SKILL_INVALID) @Positive(message = MessageConstant.SKILL_INVALID) Long> skillIds; // 技能ID列表

    @NotNull(message = MessageConstant.DISPATCH_STATUS_REQUIRED)
    private Boolean dispatchEnabled; // 是否参与派单
}
