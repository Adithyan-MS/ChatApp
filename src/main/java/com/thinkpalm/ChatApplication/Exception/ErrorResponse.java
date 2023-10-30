package com.thinkpalm.ChatApplication.Exception;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String errorCode;
    private Timestamp timestamp;
}
