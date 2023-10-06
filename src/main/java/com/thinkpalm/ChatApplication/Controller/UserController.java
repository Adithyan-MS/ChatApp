package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.UserData;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import com.thinkpalm.ChatApplication.Service.ImageService;
import com.thinkpalm.ChatApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("chatApi/v1/user")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    @Autowired
    public UserController(UserService userService,ImageService imageService){
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserData>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String,Object>> getUserDetails(@PathVariable String username){
        return new ResponseEntity<>(userService.getUserDetails(username), HttpStatus.OK);
    }

    @PostMapping("/updateUserBio")
    public ResponseEntity<String> updateUserBio(@RequestBody Map<String,String> request){
        return new ResponseEntity<>(userService.updateUserBio(request),HttpStatus.OK);
    }

    @PostMapping("/uploadUserPicture")
    public ResponseEntity<String> uploadProfilePic(@RequestParam("file") MultipartFile multipartFile){
        return new ResponseEntity<>(imageService.uploadPicture(multipartFile),HttpStatus.OK);
    }


}
