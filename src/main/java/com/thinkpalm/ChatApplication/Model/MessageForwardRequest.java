package com.thinkpalm.ChatApplication.Model;

import java.util.List;

public class MessageForwardRequest {
     private List<Integer> messageIds;
     private List<Receiver> receivers;

    public List<Integer> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Integer> messageIds) {
        this.messageIds = messageIds;
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Receiver> receivers) {
        this.receivers = receivers;
    }
}
