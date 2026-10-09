package com.homeservice.domain.dto.account;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.enums.AccountStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AccountStatusDTO {

    @NotNull(message = MessageConstant.ACCOUNT_STATUS_REQUIRED)
    private AccountStatus status; // 状态
}
