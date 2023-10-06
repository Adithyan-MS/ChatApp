package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.CreateRoomRequest;
import com.thinkpalm.ChatApplication.Service.ImageService;
import com.thinkpalm.ChatApplication.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String createRoom(@RequestBody CreateRoomRequest createRoomRequest){
        return roomService.createRoom(createRoomRequest);
    }

    @PostMapping("/{roomId}/uploadUserPicture")
    public ResponseEntity<String> uploadProfilePic(@PathVariable Integer roomId,@RequestParam("file") MultipartFile multipartFile){
        return new ResponseEntity<>(imageService.uploadPicture(roomId,multipartFile), HttpStatus.OK);
    }
}
