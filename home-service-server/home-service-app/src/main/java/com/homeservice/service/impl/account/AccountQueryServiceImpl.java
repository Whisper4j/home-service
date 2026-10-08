package com.homeservice.service.impl.account;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.homeservice.domain.po.account.AuthAccount;
import com.homeservice.domain.value.AccountIdentity;
import com.homeservice.mapper.account.AuthAccountMapper;
import com.homeservice.service.account.IAccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service @RequiredArgsConstructor
public class AccountQueryServiceImpl implements IAccountQueryService {
    private final AuthAccountMapper accountMapper;
    @Transactional(readOnly = true)
    public Optional<AccountIdentity> findByAccountId(long accountId) {
        AuthAccount account = accountMapper.selectOne(Wrappers.<AuthAccount>lambdaQuery()
            .select(AuthAccount::getId, AuthAccount::getRole, AuthAccount::getStatus).eq(AuthAccount::getId, accountId));
        return Optional.ofNullable(account).map(a -> new AccountIdentity(a.getId(), a.getRole(), a.getStatus()));
    }
}
