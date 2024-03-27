package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<String> sendMessage(@RequestBody MessageSendRequest msg) throws IllegalAccessException {
        return new ResponseEntity<>(messageService.sendMessage(msg),HttpStatus.OK);
    }
    @PostMapping("/sendFile")
    public ResponseEntity<String> sendFile(@RequestPart("file") MultipartFile file, @RequestPart("messageData") String messageSendRequestText) {
        return new ResponseEntity<>(messageService.sendFile(file,messageSendRequestText),HttpStatus.OK);
    }

    @PostMapping("/forwardMessage")
    public ResponseEntity<String> forwardMessage(@RequestBody MessageForwardRequest msg){
        return new ResponseEntity<>(messageService.forwardMessage(msg),HttpStatus.OK);
    }
    @PostMapping("/editMessage")
    public ResponseEntity<String> editMessage(@RequestBody EditRequest editRequest) throws IllegalAccessException {
        return new ResponseEntity<>(messageService.editMessage(editRequest),HttpStatus.OK);
    }

    @PostMapping("/starOrUnstarMessage")
    public ResponseEntity<String> starOrUnstarMessage(@RequestBody Map<String,List<Integer>> starRequest) {
        return new ResponseEntity<>(messageService.starOrUnstarMessage(starRequest.get("messageIds")),HttpStatus.OK);
    }

    @PostMapping("/deleteMessage")
    public ResponseEntity<String> deleteMessage(@RequestBody Map<String,List<Integer>> deleteRequest){
        return new ResponseEntity<>(messageService.deleteMessage(deleteRequest.get("messageIds")),HttpStatus.OK);
    }

//    @GetMapping("/user/{otherUserId}")
//    public ResponseEntity<List<Map<String, Object>>> getUserChatMessages(@PathVariable Integer otherUserId){
//        return new ResponseEntity<>(messageService.getUserChatMessages(otherUserId),HttpStatus.OK);
//    }
    @GetMapping("/user/{otherUserId}/page/{pageNumber}")
    public ResponseEntity<List<Map<String, Object>>> getPaginatedUserChatMessages(@PathVariable Integer otherUserId, @PathVariable Integer pageNumber){
        return new ResponseEntity<>(messageService.getPaginatedUserChatMessages(otherUserId,pageNumber),HttpStatus.OK);
    }

    @GetMapping("/user/{otherUserId}/search")
    public ResponseEntity<List<Map<String,Object>>> searchUserChats(@RequestParam("value") String searchContent,@PathVariable Integer otherUserId){
        return new ResponseEntity<>(messageService.searchUserChatMessage(otherUserId,searchContent),HttpStatus.OK);
    }

    @GetMapping("/forward/search")
    public ResponseEntity<List<Map<String,Object>>> searchAllChats(@RequestParam("value") String searchContent){
        return new ResponseEntity<>(messageService.searchAllChats(searchContent),HttpStatus.OK);
    }

//    @GetMapping("/room/{roomId}")
//    public ResponseEntity<List<Map<String,Object>>> getRoomChatMessages(@PathVariable Integer roomId) throws IllegalAccessException {
//        return new ResponseEntity<>(messageService.getRoomChatMessages(roomId), HttpStatus.OK);
//    }

    @GetMapping("/room/{roomId}/page/{pageNumber}")
    public ResponseEntity<List<Map<String,Object>>> getRoomChatMessages(@PathVariable Integer roomId,@PathVariable Integer pageNumber) throws IllegalAccessException {
        return new ResponseEntity<>(messageService.getPaginatedRoomChatMessages(roomId,pageNumber), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}/search")
    public ResponseEntity<List<Map<String,Object>>> searchRoomChats(@RequestParam("value") String searchContent,@PathVariable Integer roomId){
        return new ResponseEntity<>(messageService.searchRoomChatMessage(roomId,searchContent),HttpStatus.OK);
    }

    @PostMapping("/like/{messageId}")
    public ResponseEntity<Integer> likeOrDislikeMessage(@PathVariable Integer messageId){
        return new ResponseEntity<>(messageService.likeOrDislikeMessage(messageId),HttpStatus.OK);
    }

    @GetMapping("/likes/{messageId}")
    public ResponseEntity<List<Map<String,Object>>> getMessageLikedUsers(@PathVariable Integer messageId){
        return new ResponseEntity<>(messageService.getMessageLikedUsers(messageId),HttpStatus.OK);
    }

    @GetMapping("/view/{name}/image/{filename}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String filename, @PathVariable String name) throws IOException {
        MediaType mediaType = determineContentType(filename);
        return ResponseEntity.ok().contentType(mediaType).body(messageService.viewFile(filename,name,"image"));
    }
    @GetMapping("/view/{name}/video/{filename}")
    public ResponseEntity<byte[]> viewVideo(@PathVariable String filename, @PathVariable String name) throws IOException {
        MediaType mediaType = determineContentType(filename);
        return ResponseEntity.ok().contentType(mediaType).body(messageService.viewFile(filename,name,"video"));
    }
    @GetMapping("/view/{name}/audio/{filename}")
    public ResponseEntity<byte[]> viewAudio(@PathVariable String filename, @PathVariable String name) throws IOException {
        MediaType mediaType = determineContentType(filename);
        return ResponseEntity.ok().contentType(mediaType).body(messageService.viewFile(filename,name,"audio"));
    }
    @GetMapping("/view/{name}/thumbnail/{filename}")
    public ResponseEntity<byte[]> viewthumbnail(@PathVariable String filename, @PathVariable String name) throws IOException {
        MediaType mediaType = determineContentType(filename);
        return ResponseEntity.ok().contentType(mediaType).body(messageService.viewFile(filename,name,"thumbnail"));
    }

    @GetMapping("/view/{name}/document/{filename}")
    public ResponseEntity<ByteArrayResource> viewDocument(@PathVariable String filename, @PathVariable String name) {
        try {
            byte[] documentBytes = messageService.viewFile(filename,name,"document");
            MediaType mediaType = determineContentType(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            ByteArrayResource resource = new ByteArrayResource(documentBytes);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType determineContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (filename.endsWith(".txt")) {
            return MediaType.TEXT_PLAIN;
        } else if (filename.endsWith(".doc") || filename.endsWith(".docx")) {
            return MediaType.APPLICATION_PDF;
        } else if (filename.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        } else if (filename.endsWith(".mp4") || filename.endsWith(".avi")) {
            return MediaType.valueOf("video/mp4");
        }  else if (filename.endsWith(".mp3")) {
            return MediaType.valueOf("audio/mp3");
        } else if (filename.endsWith(".wav")) {
            return MediaType.valueOf("audio/wav");
        } else if (filename.endsWith(".ogg")) {
            return MediaType.valueOf("audio/ogg");
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
