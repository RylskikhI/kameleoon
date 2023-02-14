package com.rlsk.kameleoon.user;

import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import com.rlsk.kameleoon.user.model.User;
import com.rlsk.kameleoon.user.repository.UserRepository;
import com.rlsk.kameleoon.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private User user;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        user = new User(1L, "John", "john@gmail.com", "password", LocalDateTime.now());
    }

    @Test
    void saveNewUser() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto dto = UserMapper.toUserDto(user);
        UserDto savedDto = userService.save(dto);

        assertEquals(dto.getId(), savedDto.getId());
        assertEquals(dto.getName(), savedDto.getName());
        assertEquals(dto.getEmail(), savedDto.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }
}
