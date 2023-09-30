package com.thinkpalm.ChatApplication.Model;

import java.util.List;

public class MessageRequest {

    private String message;

    private List<String> to;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }
}
