package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.StompSendMessage;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chatApi/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> userRegister(@RequestBody  UserModel user){
        return new ResponseEntity<>(authService.registerUser(user), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> userLogin(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.loginUser(loginRequest),HttpStatus.OK);
    }

    @MessageMapping("/activateUser")
    public void activateUser(@Payload Map<String,Integer> stompMessage){
        authService.activateUser(stompMessage.get("userId"));
    }

    @MessageMapping("/deactivateUser")
    public void deactivateUser(@Payload Map<String,Integer> stompMessage){
        authService.deactivateUser(stompMessage.get("userId"));
    }

}
