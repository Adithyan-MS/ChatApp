package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = EmailValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValid {
    String message() default "Please provide a valid email id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
