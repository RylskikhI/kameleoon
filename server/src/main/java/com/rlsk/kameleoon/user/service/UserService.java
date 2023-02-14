package com.rlsk.kameleoon.user.service;

import com.rlsk.kameleoon.user.dto.UserDto;

public interface UserService {
    /**
     * Создание пользователя.
     * @param userDto Entity.
     * @return UserDto.
     */
    UserDto save(UserDto userDto);
}
