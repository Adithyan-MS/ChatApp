package com.thinkpalm.ChatApplication.Exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.sql.Timestamp;


@Getter
@Setter
public class ErrorResponse {
    private Integer Code;
    private HttpStatus status;
    private String error;
    private String message;
    private Timestamp timestamp;
}
