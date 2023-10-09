package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.EditRequest;
import com.thinkpalm.ChatApplication.Model.MessageForwardRequest;
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

    @PostMapping("/forwardMessage")
    public String forwardMessage(@RequestBody MessageForwardRequest msg){
        return messageService.forwardMessage(msg);
    }

    @PostMapping("/editMessage")
    public ResponseEntity<String> editMessage(@RequestBody EditRequest editRequest){
        return new ResponseEntity<>(messageService.editMessage(editRequest),HttpStatus.OK);
    }

    @GetMapping("/user/{otherUser}")
    public List<Map<String, Object>> getUserChatMessages(@PathVariable String otherUser){
        return messageService.getUserChatMessages(otherUser);
    }

    @GetMapping("/room/{roomName}")
    public ResponseEntity<List<Map<String,Object>>> getRoomChatMessages(@PathVariable String roomName){
        return new ResponseEntity<>(messageService.getRoomChatMessages(roomName), HttpStatus.OK);
    }

    @PostMapping("/like/{messageId}")
    public ResponseEntity<String> likeOrDislikeMessage(@PathVariable Integer messageId){
        return new ResponseEntity<>(messageService.likeOrDislikeMessage(messageId),HttpStatus.OK);
    }

    @GetMapping("/likes/users/{messageId}")
    public ResponseEntity<List<Map<String,Object>>> getMessageLikedUsers(@PathVariable Integer messageId){
        return new ResponseEntity<>(messageService.getMessageLikedUsers(messageId),HttpStatus.OK);
    }

}
