package com.thinkpalm.ChatApplication.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageSendRequest {
    private Message message;
    private Receiver receiver;

}
