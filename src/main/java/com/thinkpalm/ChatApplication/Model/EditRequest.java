package com.thinkpalm.ChatApplication.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRequest {
    private Integer messageId;
    private String newContent;

}
