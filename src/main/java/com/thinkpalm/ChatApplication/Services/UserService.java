package com.thinkpalm.ChatApplication.Services;

import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Service
public class UserService {

    private final UserRepository urep;
    @Autowired
    private PasswordEncoder encoder;

    private final JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository urep,JwtService jwtService) {
        this.urep = urep;
        this.jwtService = jwtService;
    }

    public String registerUser(UserModel user){
        try{
            user.setPassword(encoder.encode(user.getPassword()));
            Date currentTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentTime);
            Timestamp currentTimestamp = new Timestamp(calendar.getTimeInMillis());
            user.setCreated_at(currentTimestamp);

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
//            throw new UsernameNotFoundException("user not found");
            }
        }
        catch (Exception e){
            return "invalid username or password!";
        }


    }
}
