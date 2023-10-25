package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidation implements ConstraintValidator<EmailValid,String>{

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email==null){
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
