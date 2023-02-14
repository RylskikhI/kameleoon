package com.rlsk.kameleoon.user.mapper;

import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.model.User;

import java.time.LocalDateTime;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
                "password",
                LocalDateTime.now()
        );
    }
}
