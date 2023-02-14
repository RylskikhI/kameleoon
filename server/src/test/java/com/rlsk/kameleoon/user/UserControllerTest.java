package com.rlsk.kameleoon.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rlsk.kameleoon.user.controller.UserController;
import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import com.rlsk.kameleoon.user.model.User;
import com.rlsk.kameleoon.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private User user;
    private UserDto dto;
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    void init() {
        user = new User(1L, "John", "john@gmail.com", "password", LocalDateTime.now());
        dto = UserMapper.toUserDto(user);
        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    @Test
    @DisplayName("Send POST request /users")
    void testSaveNewUser() throws Exception {
        Mockito.when(userService.save(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto)))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).save(Mockito.any());
    }
}
