package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Exception.DuplicateEntryException;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Repository.TokenRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AuthService(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService, PasswordEncoder encoder, AuthenticationManager authenticationManager, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.messagingTemplate = messagingTemplate;
    }

    public Object registerUser(UserModel user){
        if(!userRepository.existByNameOrPhonenumber(user.getName(),user.getPhone_number()).isEmpty())
            throw new DuplicateEntryException("Username or Phonenumber already exist!");
        else {
            try{
                user.setPassword(encoder.encode(user.getPassword()));
                UserModel newUser = userRepository.save(user);
                String jwtToken =  jwtService.generateToken(newUser.getName());
                revokeAllUserTokens(newUser);
                saveUserToken(newUser, jwtToken);
                Map<String,String> res = new HashMap<>();
                res.put("token",jwtToken);
                return res;
            }
            catch (Exception e){
                return "Registration Failed!";
            }
        }
    }

    public Map<String,String> loginUser(LoginRequest loginRequest) {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            if (authenticate.isAuthenticated()){
                UserModel loggedInUser = userRepository.findByName(authenticate.getName()).orElse(null);
                String jwtToken = jwtService.generateToken(loggedInUser.getName());
                revokeAllUserTokens(loggedInUser);
                saveUserToken(loggedInUser,jwtToken);
                Map<String,String> res = new HashMap<>();
                res.put("token",jwtToken);
                return res;
            }
            else{
                throw new UserNotFoundException("Invalid Username or Password!");
            }
        }
        catch (Exception e){
            throw new UserNotFoundException("Invalid Username or Password!");
        }
    }

    private void revokeAllUserTokens(UserModel user){
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach((t)->{
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(UserModel user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .type(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public void activateUser(Integer userId) {
        UserModel user = userRepository.findById(userId).orElse(null);
        if(user!=null){
            user.setOnline(true);
            userRepository.save(user);
            messagingTemplate.convertAndSend("/topic/news",user.getName() + " is Online");
        }else {
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public void deactivateUser(Integer userId) {
        UserModel user = userRepository.findById(userId).orElse(null);
        if(user!=null){
            user.setOnline(false);
            userRepository.save(user);
            messagingTemplate.convertAndSend("/topic/news",user.getName() + " is Offline");
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }
}
