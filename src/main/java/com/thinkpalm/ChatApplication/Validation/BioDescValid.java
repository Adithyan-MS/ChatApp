package com.thinkpalm.ChatApplication.Validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = BioDescValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface BioDescValid {
    String message() default "Bio must be less than or equal to 200 characters!";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
