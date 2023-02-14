package com.rlsk.kameleoon.user.service;

import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import com.rlsk.kameleoon.user.model.User;
import com.rlsk.kameleoon.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User userToSave = userRepository.save(user);
        return UserMapper.toUserDto(userToSave);
    }
}
