package com.angelai.assistant.demo.langchain4j.controller;

import com.angelai.assistant.demo.langchain4j.dto.LoginRequest;
import com.angelai.assistant.demo.langchain4j.dto.RegisterRequest;
import com.angelai.assistant.demo.langchain4j.dto.UserDto;
import com.angelai.assistant.demo.langchain4j.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_shouldReturnSuccess() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest("testuser", "password123", "test@example.com");
        UserDto userDto = new UserDto(1L, "testuser", "test@example.com");

        when(userService.register(any(RegisterRequest.class))).thenReturn(userDto);

        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value("testuser"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void login_shouldReturnSuccess() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");
        UserDto userDto = new UserDto(1L, "testuser", "test@example.com");

        when(userService.login(any(LoginRequest.class))).thenReturn(userDto);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.username").value("testuser"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void register_withExistingUsername_shouldReturnError() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest("testuser", "password123", "test@example.com");

        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException("用户名已存在"));

        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturnError() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        when(userService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("密码错误"));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }
}