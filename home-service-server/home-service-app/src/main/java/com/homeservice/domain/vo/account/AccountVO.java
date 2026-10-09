package com.homeservice.domain.vo.account;

import com.homeservice.enums.AccountStatus;
import com.homeservice.enums.Role;
import com.homeservice.handler.json.ApiId;

import lombok.Data;

@Data
public class AccountVO {

    @ApiId
    private Long id; // 主键ID

    private String username; // 用户名

    private String displayName; // 显示名称

    private Role role; // 账号角色

    private AccountStatus status; // 状态

    private String phone; // 手机号
}
