package com.thinkpalm.ChatApplication.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageForwardRequest {
     private List<Integer> messageIds;
     private List<Receiver> receivers;

}
