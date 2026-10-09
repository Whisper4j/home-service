package com.homeservice.domain.query.account;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.enums.AccountStatus;
import com.homeservice.enums.Role;

import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = MessageConstant.SEARCH_KEYWORD_TOO_LONG)
    private String keyword; // 搜索关键词
    private Role role; // 账号角色
    private AccountStatus status; // 状态

}
