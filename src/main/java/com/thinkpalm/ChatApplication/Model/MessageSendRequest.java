package com.thinkpalm.ChatApplication.Model;

import java.util.List;

public class MessageSendRequest {
    private Message message;
    private Receiver receiver;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
