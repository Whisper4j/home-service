package com.homeservice.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.nio.charset.StandardCharsets;
/** 入口与工具层都检查 UTF-8 字节数，防止 BCrypt 隐式截断。 */
public final class StrictBcryptPasswordEncoder implements PasswordEncoder {
    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();
    private static void check(CharSequence value) {
        if (value == null || value.toString().getBytes(StandardCharsets.UTF_8).length > 72)
            throw new IllegalArgumentException("密码 UTF-8 编码不能超过 72 字节");
    }
    public String encode(CharSequence raw) { check(raw); return delegate.encode(raw); }
    public boolean matches(CharSequence raw, String encoded) { check(raw); return delegate.matches(raw, encoded); }
    public boolean upgradeEncoding(String encoded) { return delegate.upgradeEncoding(encoded); }
}
