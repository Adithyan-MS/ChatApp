package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UpdateBioRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Service.ImageService;
import com.thinkpalm.ChatApplication.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("chatApi/v1/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserModel> getUserDetails(@PathVariable String username){
        return new ResponseEntity<>(userService.getUserDetails(username), HttpStatus.OK);
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Map<String,Object>>> getAllChatsOfUsers(){
        return new ResponseEntity<>(userService.getAllChatsOfUsers(),HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Map<String,Object>>> searchChats(@RequestParam("name") String searchName){
        return new ResponseEntity<>(userService.searchChats(searchName),HttpStatus.OK);
    }
    @GetMapping("/searchUsers")
    public ResponseEntity<List<Map<String,Object>>> searchUsers(@RequestParam("name") String searchName){
        return new ResponseEntity<>(userService.searchUsers(searchName),HttpStatus.OK);
    }

    @PostMapping("/update/bio")
    public ResponseEntity<String> updateUserBio(@Valid @RequestBody UpdateBioRequest request){
        return new ResponseEntity<>(userService.updateUserBio(request),HttpStatus.OK);
    }

    @GetMapping("/{otherUserName}/commonRooms")
    public ResponseEntity<List<Map<String, Object>>> getCommonRooms(@PathVariable String otherUserName){
        return new ResponseEntity<>(userService.getCommonRooms(otherUserName),HttpStatus.OK);
    }
    @GetMapping("/starredMessages")
    public ResponseEntity<List<Map<String, Object>>> getStarredMessages(){
        return new ResponseEntity<>(userService.getStarredMessages(),HttpStatus.OK);
    }
    @GetMapping("/starredMessages/search")
    public ResponseEntity<List<Map<String, Object>>> searchStarredMessageChats(@RequestParam("name") String searchName){
        return new ResponseEntity<>(userService.searchStarredMessageChats(searchName),HttpStatus.OK);
    }

}
