package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.MessageSendRequest;
import com.thinkpalm.ChatApplication.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatApi/v1/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody MessageSendRequest msg){
        return messageService.sendMessage(msg);
    }

}
