package com.jeevanBank.jeevan.mapper;

import com.jeevanBank.jeevan.dto.UserDto;
import com.jeevanBank.jeevan.entity.User;

import java.sql.Timestamp;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getPhone_number(),
                user.getAddress(),
                user.getBalance(),
                user.getAccountNumber(),
                user.getCreate_at(),
                user.getUpdate_at(),
                user.getDeleted_at()
        );
    }
    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getUser_id(),
                userDto.getFirst_name(),
                userDto.getLast_name(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getPhone_number(),
                userDto.getAddress(),
                userDto.getBalance(),
                userDto.getAccount_number(),
                userDto.getCreate_at(),
                userDto.getUpdate_at(),
                userDto.getDeleted_at()
        );
    }
}
