package com.homeservice.validation;
import jakarta.validation.*;
public class MultipleOfValidator implements ConstraintValidator<MultipleOf, Integer> {
    private int divisor; public void initialize(MultipleOf annotation) { divisor = annotation.value(); }
    public boolean isValid(Integer value, ConstraintValidatorContext context) { return value == null || value % divisor == 0; }
}
