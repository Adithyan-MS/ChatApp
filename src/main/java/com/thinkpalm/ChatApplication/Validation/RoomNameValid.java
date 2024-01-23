package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = RoomNameValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RoomNameValid {
    String message() default "Please provide a valid room name!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
