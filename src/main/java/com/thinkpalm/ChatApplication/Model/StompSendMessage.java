package com.thinkpalm.ChatApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StompSendMessage {
    private Integer senderId;
    private ReceiverType receiverType;
    private Integer receiverId;
}
