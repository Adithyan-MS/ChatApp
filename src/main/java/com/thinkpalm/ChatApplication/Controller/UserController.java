package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Service.ImageService;
import com.thinkpalm.ChatApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserModel> getUserDetails(@PathVariable String username){
        return new ResponseEntity<>(userService.getUserDetails(username), HttpStatus.OK);
    }

    @PostMapping("/updateUserBio")
    public ResponseEntity<String> updateUserBio(@RequestBody Map<String,String> request){
        return new ResponseEntity<>(userService.updateUserBio(request),HttpStatus.OK);
    }

    @PostMapping("/uploadUserPicture")
    public ResponseEntity<String> uploadProfilePic(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(imageService.uploadPicture(multipartFile),HttpStatus.OK);
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String filename) throws IOException {
        String contentType = determineContentType(filename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(imageService.viewImage(filename));
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }


}
