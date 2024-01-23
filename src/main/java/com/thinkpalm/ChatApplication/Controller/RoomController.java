package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Service.ImageService;
import com.thinkpalm.ChatApplication.Service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatApi/v1/room")
public class RoomController {

    private final RoomService roomService;
    private final ImageService imageService;

    @Autowired
    public RoomController(RoomService roomService,ImageService imageService)
    {
        this.roomService = roomService;
        this.imageService = imageService;
    }

    @PostMapping("/createRoom")
    public ResponseEntity<RoomModel> createRoom(@Valid @RequestBody CreateRoomRequest createRoomRequest) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.createRoom(createRoomRequest),HttpStatus.OK);
    }

    @PostMapping("/{roomId}/addMember")
    public ResponseEntity<String> addMember(@PathVariable Integer roomId, @RequestBody Map<String,List<Integer>> request) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.addMember(roomId,request.get("members")),HttpStatus.OK);
    }

    @PostMapping("/{roomId}/removeMember")
    public ResponseEntity<String> removeMember(@PathVariable Integer roomId, @RequestBody Map<String,List<Integer>> request) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.removeMember(roomId,request.get("members")),HttpStatus.OK);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<String> joinRoom(@PathVariable Integer roomId) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.joinRoom(roomId),HttpStatus.OK);
    }

    @PostMapping("/{roomId}/exitRoom")
    public ResponseEntity<String> exitRoom(@PathVariable Integer roomId) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.exitRoom(roomId),HttpStatus.OK);
    }

    @PostMapping("/{roomId}/makeRoomAdmin/{otherUserId}")
    public ResponseEntity<String> makeRoomAdmin(@PathVariable Integer roomId,@PathVariable Integer otherUserId) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.makeRoomAdmin(roomId,otherUserId),HttpStatus.OK);
    }

    @PostMapping("/{roomId}/dismissRoomAdmin/{otherUserId}")
    public ResponseEntity<String> dismissRoomAdmin(@PathVariable Integer roomId,@PathVariable Integer otherUserId) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.dismissRoomAdmin(roomId,otherUserId),HttpStatus.OK);
    }

    @GetMapping("/{roomId}/getRoomCode")
    public ResponseEntity<String> getRoomCode(@PathVariable Integer roomId) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.getRoomCode(roomId),HttpStatus.OK);
    }
    @GetMapping("/roomCode/{roomCode}")
    public ResponseEntity<Map<String,Object>> getRoomByRoomCode(@PathVariable String roomCode) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.getRoomByRoomCode(roomCode),HttpStatus.OK);
    }
    @GetMapping("/{roomId}/isParticipant/{userId}")
    public ResponseEntity<Boolean> IsUserPartcicpant(@PathVariable Integer roomId,@PathVariable Integer userId){
        return new ResponseEntity<>(roomService.IsUserPartcicpant(roomId,userId),HttpStatus.OK);
    }

    @GetMapping("/{roomId}/participants")
    public ResponseEntity<List<Map<String, Object>>> getRoomPartcicpants(@PathVariable Integer roomId){
        return new ResponseEntity<>(roomService.getRoomParticipants(roomId),HttpStatus.OK);
    }

    @GetMapping("/{roomId}/pastParticipants")
    public ResponseEntity<List<Map<String, Object>>> getPastRoomParticipants(@PathVariable Integer roomId){
        return new ResponseEntity<>(roomService.getPastRoomParticipants(roomId),HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public  ResponseEntity<RoomModel> getroomDetails(@PathVariable Integer roomId){
        return new ResponseEntity<>(roomService.getRoomDetails(roomId),HttpStatus.OK);
    }
    @PostMapping("/{roomId}/delete")
    public  ResponseEntity<String> deleteRoomforUser(@PathVariable Integer roomId){
        return new ResponseEntity<>(roomService.deleteRoomforUser(roomId),HttpStatus.OK);
    }
    @PostMapping("/{roomId}/change/name")
    public  ResponseEntity<String> changeRoomName(@PathVariable Integer roomId,@Valid @RequestBody RoomNameChangeRequest roomNameChangeRequest) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.changeName(roomId,roomNameChangeRequest.getName()),HttpStatus.OK);
    }
    @PostMapping("/{roomId}/change/desc")
    public  ResponseEntity<String> changeDescriptionName(@PathVariable Integer roomId,@Valid @RequestBody RoomDescChangeRequest roomDescriptionChangeRequest) throws IllegalAccessException {
        return new ResponseEntity<>(roomService.changeDescription(roomId,roomDescriptionChangeRequest.getDescription()),HttpStatus.OK);
    }


}
