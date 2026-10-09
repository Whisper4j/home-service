package com.homeservice.domain.dto.account;

import com.homeservice.enums.AccountStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AccountStatusDTO {

    @NotNull(message = "状态不能为空")
    private AccountStatus status; // 状态
}
