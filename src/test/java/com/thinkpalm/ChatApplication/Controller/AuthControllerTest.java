package com.thinkpalm.ChatApplication.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkpalm.ChatApplication.Exception.DuplicateEntryException;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Model.LoginRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testUserRegister_Success() throws Exception {
        UserModel user = UserModel.builder()
                .name("sharon")
                .email("sharon@gmail.com")
                .password("sharon123")
                .phone_number("0023453213")
                .build();

        when(authService.registerUser(any(UserModel.class))).thenReturn("jwtToken");

        mockMvc.perform(MockMvcRequestBuilders.post("/chatApi/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwtToken"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUserRegister_DuplicateEntry() throws Exception {
        UserModel user = UserModel.builder()
                .name("jebin")
                .email("jebin@gmail.com")
                .password("jebin123")
                .phone_number("0009453203")
                .build();

        when(authService.registerUser(any(UserModel.class))).thenThrow(new DuplicateEntryException("Username already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/chatApi/v1/auth/register" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user))
                )
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateEntryException))
                .andExpect(result -> assertEquals("Username already exists", result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());
   }

    @Test
    public void testUserLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest("validUsername", "validPassword");
        when(authService.loginUser(any(LoginRequest.class))).thenReturn("jwtToken");

        mockMvc.perform(MockMvcRequestBuilders.post("/chatApi/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwtToken"));
    }

    @Test
    public void testUserLogin_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("invalidUsername", "invalidPassword");
        when(authService.loginUser(any(LoginRequest.class)))
                .thenThrow(new UserNotFoundException("invalid username or password!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/chatApi/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("invalid username or password!", result.getResolvedException().getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }
    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}