package com.jeevanBank.jeevan.service;

import com.jeevanBank.jeevan.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long user_id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long user_id, UserDto updateUserDto);
    void deleteUser(Long user_id);
}
