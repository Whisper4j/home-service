package com.homeservice.validation;
import jakarta.validation.*;
import java.lang.annotation.*;
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Utf8PasswordValidator.class)
public @interface Utf8Password {
    String message() default "密码 UTF-8 编码不能超过 72 字节";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
}
