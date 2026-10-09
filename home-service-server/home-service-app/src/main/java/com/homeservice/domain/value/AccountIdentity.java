package com.homeservice.domain.value;

import com.homeservice.enums.AccountStatus;
import com.homeservice.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountIdentity {

    private Long accountId; // 账号ID
    private Role role; // 账号角色
    private AccountStatus status; // 账号状态
}
