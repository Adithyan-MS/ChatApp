package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatApi/v1/auth")
public class AuthController {

    private final AuthService userv;

    @Autowired
    public AuthController(AuthService userv){
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

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }



}
