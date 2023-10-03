package com.thinkpalm.ChatApplication.Model;

public class Message {
    private String content;
    private Integer parentMessage;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Integer parentMessage) {
        this.parentMessage = parentMessage;
    }
}
