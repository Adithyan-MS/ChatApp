package com.thinkpalm.ChatApplication.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String content;
    private Integer parentMessage;

}
