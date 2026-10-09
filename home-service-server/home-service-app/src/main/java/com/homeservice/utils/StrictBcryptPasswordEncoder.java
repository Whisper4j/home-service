package com.homeservice.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

/**
 * BCrypt密码编码器类
 * 校验密码字节长度并执行BCrypt编码匹配
 */
public final class StrictBcryptPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

    /**
     * 校验输入数据
     */
    private static void check(CharSequence value) {
        if (value == null || value.toString().getBytes(StandardCharsets.UTF_8).length > 72)
            throw new IllegalArgumentException("密码 UTF-8 编码不能超过 72 字节");
    }

    /**
     * 编码BCrypt密码
     */
    public String encode(CharSequence raw) {
        check(raw);
        return delegate.encode(raw);
    }

    /**
     * 校验明文密码与密文是否匹配
     */
    public boolean matches(CharSequence raw, String encoded) {
        check(raw);
        return delegate.matches(raw, encoded);
    }

    /**
     * 判断密码密文是否需要升级
     */
    public boolean upgradeEncoding(String encoded) {
        return delegate.upgradeEncoding(encoded);
    }
}
