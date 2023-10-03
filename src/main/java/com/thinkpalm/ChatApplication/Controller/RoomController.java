package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.CreateRoomRequest;
import com.thinkpalm.ChatApplication.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatApi/v1/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/createRoom")
    public String createRoom(@RequestBody CreateRoomRequest createRoomRequest){
        return roomService.createRoom(createRoomRequest);
    }
}
