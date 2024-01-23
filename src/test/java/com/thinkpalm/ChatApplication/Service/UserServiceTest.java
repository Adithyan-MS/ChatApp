package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.UpdateBioRequest;
import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
                .name("wick")
                .email("wick@gmail.com")
                .password("wick123")
                .phone_number("0023453213")
                .build();
        when(userRepository.findByName(user.getName())).thenReturn(Optional.ofNullable(user));
        assertThat(userService.getUserDetails(user.getName())).isEqualTo(user);
    }
    @Test
    void getUserDetails_Exception() {
        UserModel user = UserModel.builder()
                .id(89)
                .name("wick")
                .email("wick@gmail.com")
                .password("wick123")
                .phone_number("0023453213")
                .build();
        when(userRepository.findByName(user.getName())).thenReturn(Optional.ofNullable(null));
        assertThrows(UserNotFoundException.class,
                ()-> userService.getUserDetails(user.getName()));
    }
    @Test
    void updateUserBio_Success() {
        UserModel user = UserModel.builder()
                .id(89)
                .name("wick")
                .email("wick@gmail.com")
                .password("wick123")
                .phone_number("0023453213")
                .build();
        UpdateBioRequest request = new UpdateBioRequest();
        request.setBio("some bio");
        try (MockedStatic mocked = mockStatic(AppContext.class)) {
            when(AppContext.getUserName()).thenReturn(user.getName());
            assertEquals(userService.updateUserBio(request),"Bio updated");
        }
    }
    @Test
    void updateUserBio_Exception() {
        UserModel user = UserModel.builder()
                .id(89)
                .name("wick")
                .email("wick@gmail.com")
                .password("wick123")
                .phone_number("0023453213")
                .build();
        UpdateBioRequest request = new UpdateBioRequest();
        request.setBio("some bio");
        try (MockedStatic mocked = mockStatic(AppContext.class)) {
            when(AppContext.getUserName()).thenReturn("");
            assertThrows(UserNotFoundException.class,
                    ()->userService.updateUserBio(request));
        }
    }
    @Test
    void getAllUsers() {
    }
    @Test
    void getAllChatsOfUsers() {
    }
}