package com.homeservice.utils;

import com.homeservice.domain.value.AccountPrincipal;
import com.homeservice.enums.ErrorCode;
import com.homeservice.exception.ApiException;

/**
 * 用户上下文工具类
 * 保存和清理当前请求的账号身份
 */
public final class UserContext {

    private static final ThreadLocal<AccountPrincipal> CURRENT = new ThreadLocal<>();

    /**
     * 创建用户上下文实例
     */
    private UserContext() {
    }

    /**
     * 保存当前请求的账号身份
     */
    public static void set(AccountPrincipal principal) {
        CURRENT.set(principal);
    }

    /**
     * 获取当前请求的账号身份
     */
    public static AccountPrincipal get() {
        return CURRENT.get();
    }

    /**
     * 获取并校验当前请求的账号身份
     */
    public static AccountPrincipal require() {
        if (get() == null) throw new ApiException(ErrorCode.UNAUTHENTICATED);
        return get();
    }

    /**
     * 清理当前请求的账号身份
     */
    public static void clear() {
        CURRENT.remove();
    }
}
