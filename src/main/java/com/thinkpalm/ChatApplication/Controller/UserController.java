package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Services.JwtService;
import com.thinkpalm.ChatApplication.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatApi/v1/user")
public class UserController {

    private final UserService userv;

    @Autowired
    public UserController(UserService userv){
        this.userv = userv;
    }

    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@RequestBody UserModel user){
        String res = userv.registerUser(user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

     @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userv.loginUser(loginRequest),HttpStatus.OK);
    }

    @GetMapping("/hey")
     public String getRes(){
        return "heyt";
     }

    @GetMapping("/hello")
    public String getRe(){
        return "heyt";
    }

}
