package com.homeservice.domain.dto.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.ApiIds;
import com.homeservice.handler.json.PasswordDeserializer;
import com.homeservice.validation.Utf8Password;

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
public class WorkerCreateDTO {

    @Size(max = 32, message = "用户名长度不能超过32")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,31}$", message = "用户名格式不正确")
    private String username; // 用户名

    @NotNull(message = "密码不能为空")
    @Size(min = 8, max = 72, message = "密码长度必须在8到72之间")
    @Utf8Password(message = "密码的UTF-8编码不能超过72字节")
    @JsonDeserialize(using = PasswordDeserializer.class)
    private String password; // 密码

    @Size(max = 40, message = "显示名称长度不能超过40")
    @NotBlank(message = "显示名称不能为空")
    private String displayName; // 显示名称

    @Size(max = 11, message = "手机号长度不能超过11")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    private String phone; // 手机号

    @Size(max = 6, message = "城市编码长度不能超过6")
    @NotBlank(message = "城市编码不能为空")
    private String cityCode; // 城市编码

    @NotNull(message = "技能ID列表不能为空")
    @Size(min = 1, message = "技能ID列表至少包含1项")
    @UniqueElements(message = "技能ID列表不能重复")
    @ApiIds
    @Valid
    private List<@NotNull(message = "技能ID列表元素不能为空") @Positive(message = "技能ID列表元素必须大于0") Long> skillIds; // 技能ID列表

    @NotNull(message = "是否参与派单不能为空")
    private Boolean dispatchEnabled; // 是否参与派单

    @Override
    public String toString() {
        return "WorkerCreateDTO[credentials=REDACTED]";
    }
}
