package com.homeservice.utils;
import com.homeservice.domain.value.AccountPrincipal;
import com.homeservice.enums.ErrorCode;
import com.homeservice.exception.ApiException;
/** 仅限当前同步 HTTP 调用栈；异步任务和 WebSocket 必须显式传递身份。 */
public final class UserContext {
    private static final ThreadLocal<AccountPrincipal> CURRENT = new ThreadLocal<>();
    private UserContext() {}
    public static void set(AccountPrincipal principal) { CURRENT.set(principal); }
    public static AccountPrincipal get() { return CURRENT.get(); }
    public static AccountPrincipal require() {
        if (get() == null) throw new ApiException(ErrorCode.UNAUTHENTICATED);
        return get();
    }
    public static void clear() { CURRENT.remove(); }
}
