package com.thinkpalm.ChatApplication.Exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String msg) {
        super(msg);
    }
}
