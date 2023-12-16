package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.EditRequest;
import com.thinkpalm.ChatApplication.Model.MessageForwardRequest;
import com.thinkpalm.ChatApplication.Model.MessageModel;
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
    public ResponseEntity<String> sendMessage(@RequestBody MessageSendRequest msg){
        return new ResponseEntity<>(messageService.sendMessage(msg),HttpStatus.OK);
    }

    @PostMapping("/forwardMessage")
    public ResponseEntity<String> forwardMessage(@RequestBody MessageForwardRequest msg){
        return new ResponseEntity<>(messageService.forwardMessage(msg),HttpStatus.OK);
    }

    @PostMapping("/editMessage")
    public ResponseEntity<String> editMessage(@RequestBody EditRequest editRequest){
        return new ResponseEntity<>(messageService.editMessage(editRequest),HttpStatus.OK);
    }

    @PostMapping("/deleteMessage")
    public ResponseEntity<String> deleteMessage(@RequestBody Map<String,List<Integer>> deleteRequest){
        return new ResponseEntity<>(messageService.deleteMessage(deleteRequest.get("messageIds")),HttpStatus.OK);
    }

    @GetMapping("/user/{otherUserId}")
    public ResponseEntity<List<Map<String, Object>>> getUserChatMessages(@PathVariable Integer otherUserId){
        return new ResponseEntity<>(messageService.getUserChatMessages(otherUserId),HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Map<String,Object>>> getRoomChatMessages(@PathVariable Integer roomId){
        return new ResponseEntity<>(messageService.getRoomChatMessages(roomId), HttpStatus.OK);
    }

    @PostMapping("/like/{messageId}")
    public ResponseEntity<Integer> likeOrDislikeMessage(@PathVariable Integer messageId){
        return new ResponseEntity<>(messageService.likeOrDislikeMessage(messageId),HttpStatus.OK);
    }

    @GetMapping("/likes/{messageId}")
    public ResponseEntity<List<Map<String,Object>>> getMessageLikedUsers(@PathVariable Integer messageId){
        return new ResponseEntity<>(messageService.getMessageLikedUsers(messageId),HttpStatus.OK);
    }

}
