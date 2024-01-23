package com.thinkpalm.ChatApplication.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoomNameValidation implements ConstraintValidator<RoomNameValid,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String roomName = s;
        int len = roomName.length();
        return len <= 40;
    }
}
