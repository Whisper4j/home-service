package com.homeservice.validation;
import jakarta.validation.*;
public class Utf8PasswordValidator implements ConstraintValidator<Utf8Password, String> {
    
    public boolean isValid(String value, ConstraintValidatorContext context) { return value == null || value.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72; }
}
