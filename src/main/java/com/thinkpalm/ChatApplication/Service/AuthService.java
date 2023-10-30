package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Exception.DuplicateEntryException;
import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.Token;
import com.thinkpalm.ChatApplication.Model.TokenType;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.TokenRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthService(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    public String registerUser(UserModel user){
        if(!userRepository.existByNameOrPhonenumber(user.getName(),user.getPhone_number()).isEmpty())
            throw new DuplicateEntryException("username or phonenumber already exist!");
        else {
            try{
                user.setPassword(encoder.encode(user.getPassword()));
                UserModel newUser = userRepository.save(user);
                String jwtToken =  jwtService.generateToken(user.getName());
                revokeAllUserTokens(newUser);
                saveUserToken(newUser, jwtToken);
                return jwtToken;
            }
            catch (Exception e){
                return "Registration Failed!";
            }
        }
    }

    public String loginUser(LoginRequest loginRequest) {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            if (authenticate.isAuthenticated()){
                UserModel loggedInUser = userRepository.findByName(authenticate.getName()).orElse(null);
                String jwtToken = jwtService.generateToken(loginRequest.getUsername());
                revokeAllUserTokens(loggedInUser);
                saveUserToken(loggedInUser,jwtToken);
                return jwtToken;
            }
            else{
                throw new UsernameNotFoundException("invalid username or password!");
            }
        }
        catch (Exception e){
            throw new UsernameNotFoundException("invalid username or password!");
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
