package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.MessageSendRequest;
import com.thinkpalm.ChatApplication.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/user/{otherUser}")
    private List<Map<String, Object>> getUserChatMessages(@PathVariable String otherUser){
        return messageService.getUserChatMessages(otherUser);
    }

    @GetMapping("/room/{roomName}")
    private ResponseEntity<List<Map<String,Object>>> getRoomChatMessages(@PathVariable String roomName){
        return new ResponseEntity<>(messageService.getRoomChatMessages(roomName), HttpStatus.OK);
    }



}
