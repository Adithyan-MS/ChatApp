package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Service
public class AuthService {

    private final UserRepository urep;

    private final PasswordEncoder encoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository urep, JwtService jwtService,PasswordEncoder encoder,AuthenticationManager authenticationManager) {
        this.urep = urep;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    public String registerUser(UserModel user){
        try{
            user.setPassword(encoder.encode(user.getPassword()));
            urep.save(user);
            return "success";
        }
        catch (Exception e){
            return "Registration Unsuccessfull";
        }
    }

    public String loginUser(LoginRequest loginRequest) {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            if (authenticate.isAuthenticated()){
                return jwtService.generateToken(loginRequest.getUsername());
            }
            else{
                return "invalid username or password!";
            }
        }
        catch (Exception e){
            return "invalid username or password!";
        }
    }
}
