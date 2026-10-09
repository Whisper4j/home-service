package com.homeservice.domain.query.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 账号查询类
 * 封装账号相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountStatus status;

}
