package com.rlsk.kameleoon.user;

import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import com.rlsk.kameleoon.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static final User USER = new User(1L, "John", "john@gmail.com", "password", LocalDateTime.now());
    private static final UserDto DTO = UserMapper.toUserDto(USER);

    @Test
    void toUserDto() {
        UserDto dto = UserMapper.toUserDto(USER);

        assertEquals(USER.getId(), dto.getId());
        assertEquals(USER.getName(), dto.getName());
        assertEquals(USER.getEmail(), dto.getEmail());
    }

    @Test
    void toUser() {
        User user = UserMapper.toUser(DTO);

        assertEquals(DTO.getId(), user.getId());
        assertEquals(DTO.getName(), user.getName());
        assertEquals(DTO.getEmail(), user.getEmail());
    }
}
