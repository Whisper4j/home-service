package com.homeservice.utils;
import com.homeservice.domain.value.AccountPrincipal;
import com.homeservice.enums.*;
import com.homeservice.exception.ApiException;
import com.homeservice.service.account.IAccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component @RequiredArgsConstructor
public class AccountAuthenticator {
    private final JwtTool jwtTool;
    private final IAccountQueryService accountQueryService;
    public AccountPrincipal authenticate(String token) { return check(jwtTool.parse(token)); }
    public AccountPrincipal check(AccountPrincipal principal) {
        var account = accountQueryService.findByAccountId(principal.accountId())
            .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHENTICATED));
        if (account.status() != AccountStatus.ENABLED) throw new ApiException(ErrorCode.ACCOUNT_DISABLED);
        if (account.role() != principal.role() || account.accountId() != principal.accountId()) throw new ApiException(ErrorCode.FORBIDDEN);
        return principal;
    }
}
