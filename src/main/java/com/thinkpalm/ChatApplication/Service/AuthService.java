package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.Token;
import com.thinkpalm.ChatApplication.Model.TokenType;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.TokenRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository urep;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthService(UserRepository urep, TokenRepository tokenRepository, JwtService jwtService, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.urep = urep;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    public String registerUser(UserModel user){
        try{
            if(!urep.existByNameOrPhonenumber(user.getName(),user.getPhone_number()).isEmpty())
                return "username or phone-number already exists!";
            else {
                user.setPassword(encoder.encode(user.getPassword()));
                UserModel newUser = urep.save(user);
                String jwtToken =  jwtService.generateToken(user.getName());
                revokeAllUserTokens(newUser);
                saveUserToken(newUser, jwtToken);
                return jwtToken;
            }
        }
        catch (Exception e){
            return "Registration Failed!";
        }
    }

    public String loginUser(LoginRequest loginRequest) {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            if (authenticate.isAuthenticated()){
                UserModel loggedInUser = urep.findByName(authenticate.getName()).orElse(null);
                String jwtToken = jwtService.generateToken(loginRequest.getUsername());
                revokeAllUserTokens(loggedInUser);
                saveUserToken(loggedInUser,jwtToken);
                return jwtToken;
            }
            else{
                return "invalid username or password!";
            }
        }
        catch (Exception e){
            return "invalid username or password!";
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
}
