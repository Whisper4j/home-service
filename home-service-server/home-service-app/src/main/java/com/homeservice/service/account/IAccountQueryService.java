package com.homeservice.service.account;
import com.homeservice.domain.value.AccountIdentity;
import java.util.Optional;
/** 仅供 HTTP/WS 认证复核真实账号；不实现登录注册或人员业务。 */
public interface IAccountQueryService { Optional<AccountIdentity> findByAccountId(long accountId); }
