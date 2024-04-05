package com.thinkpalm.ChatApplication.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder

public class ChatNotification {
    private ReceiverType senderType;
    private Integer senderId;
}
