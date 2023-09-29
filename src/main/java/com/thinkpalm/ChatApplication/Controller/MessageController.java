package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chatApi/v1/")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/sendPersonalMessage/{receiverName}")
    public String sendPersonalMessage(@PathVariable String receiverName, @RequestBody Map<String,String> msg){
        return messageService.sendPersonalMessage(receiverName,msg);
    }

}
