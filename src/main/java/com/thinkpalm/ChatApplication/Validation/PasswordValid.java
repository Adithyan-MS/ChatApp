package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = PasswordValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValid {
    String message() default "Please provide a valid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
