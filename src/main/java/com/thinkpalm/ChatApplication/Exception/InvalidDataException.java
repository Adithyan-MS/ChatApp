package com.thinkpalm.ChatApplication.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String s) {
        super(s);
    }
}
