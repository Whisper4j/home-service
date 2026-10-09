package com.homeservice.service.account;

import com.homeservice.domain.value.AccountIdentity;

import java.util.Optional;

/**
 * 账号查询服务接口
 * 定义账号查询相关服务能力
 */
public interface IAccountQueryService {
    /**
     * 根据账号编号查询账号身份
     */
    Optional<AccountIdentity> findByAccountId(long accountId);
}
