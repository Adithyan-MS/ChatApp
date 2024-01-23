package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class BioDescValidation implements ConstraintValidator<BioDescValid,String> {

    @Override
    public boolean isValid(String bio, ConstraintValidatorContext constraintValidatorContext) {
        int len = bio.length();
        return len <= 200;
    }
}
