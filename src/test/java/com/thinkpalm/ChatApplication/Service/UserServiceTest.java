package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserDetails_Success() {
        UserModel user = UserModel.builder()
                .id(89)
                .name("sharon")
                .email("sharon@gmail.com")
                .password("sharon123")
                .phone_number("0023453213")
                .build();
        when(userRepository.findByName(any(String.class))).thenReturn(Optional.ofNullable(user));
        assertThat(userService.getUserDetails(any(String.class))).isEqualTo(user);
    }

    @Test
    void updateUserBio() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getAllChatsOfUsers() {
    }
}