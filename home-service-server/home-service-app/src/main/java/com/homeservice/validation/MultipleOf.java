package com.homeservice.validation;
import jakarta.validation.*;
import java.lang.annotation.*;
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipleOfValidator.class)
public @interface MultipleOf {
    String message() default "数值必须符合指定粒度";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}
