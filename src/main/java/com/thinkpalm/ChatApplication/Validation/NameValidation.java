package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidation implements ConstraintValidator<NameValid,String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String name  = s.trim();
        if(name.isBlank()){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Name can't be empty").addConstraintViolation();
            return false;
        }
        int len = s.length();
        return len >= 3 && len <= 15;
    }
}
