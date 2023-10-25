package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jdk.jfr.ContentType;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = NameValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NameValid {
    String message() default "Please provide a valid name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
