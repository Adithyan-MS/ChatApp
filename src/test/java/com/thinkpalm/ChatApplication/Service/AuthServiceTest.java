package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Exception.DuplicateEntryException;
import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.TokenRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private Authentication authentication;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserModel user = UserModel.builder()
                .name("sharon")
                .email("sharon@gmail.com")
                .password("sharon123")
                .phone_number("0023453213")
                .build();

        when(userRepository.existByNameOrPhonenumber(user.getName(), user.getPhone_number()))
                .thenReturn(Collections.emptyList());
        when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(user.getName())).thenReturn("jwtToken");
        UserModel savedUser = new UserModel();
        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);

        assertEquals("jwtToken", authService.registerUser(user));
    }

    @Test
    public void testRegisterUser_DuplicateEntryException() {
        UserModel user = UserModel.builder()
                .name("sharon")
                .email("sharon@gmail.com")
                .password("sharon123")
                .phone_number("0023453213")
                .build();

        when(userRepository.existByNameOrPhonenumber(user.getName(), user.getPhone_number())).thenReturn(List.of(user));

        try {
            authService.registerUser(user);
            fail("Expected DuplicateEntryException");
        } catch (DuplicateEntryException e) {
            assertEquals("username or phonenumber already exist!", e.getMessage());
        }
    }
    @Test
    void testLoginUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sharon");
        loginRequest.setPassword("sharon123");
        UserModel user = UserModel.builder()
                .id(89)
                .name("sharon")
                .email("sharon@gmail.com")
                .password("sharon123")
                .phone_number("0023453213")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findByName(authentication.getName())).thenReturn(Optional.ofNullable(user));
        when(jwtService.generateToken(loginRequest.getUsername())).thenReturn("someToken");

        assertEquals("someToken", authService.loginUser(loginRequest));
    }
    @Test
    void testLoginUser_Exception1(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sharon");
        loginRequest.setPassword("sharon123");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        try {
            authService.loginUser(loginRequest);
            fail("Expected UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {
            assertEquals("invalid username or password!", e.getMessage());
        }
    }

    @Test
    void testLoginUser_Exception2(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sharon");
        loginRequest.setPassword("sharon123");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Something Went Wrong"));

        try {
            authService.loginUser(loginRequest);
            fail("Expected UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {
            assertEquals("invalid username or password!", e.getMessage());
        }
    }
}
