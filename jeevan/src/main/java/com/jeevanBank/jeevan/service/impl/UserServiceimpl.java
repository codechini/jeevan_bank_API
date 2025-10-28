package com.jeevanBank.jeevan.service.impl;

import com.jeevanBank.jeevan.dto.UserDto;
import com.jeevanBank.jeevan.entity.User;
import com.jeevanBank.jeevan.exception.ResouceNotFound;
import com.jeevanBank.jeevan.mapper.UserMapper;
import com.jeevanBank.jeevan.repository.UserRepository;
import com.jeevanBank.jeevan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceimpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User result = userRepository.save(user);
        return UserMapper.toUserDto(result);
    }

    @Override
    public UserDto getUserById(Long user_id) {
        User user = userRepository.findById(Math.toIntExact(user_id)).orElseThrow(()-> new ResouceNotFound("id", user_id, "User"));
        return UserMapper.toUserDto(user);

    }

    @Override
    public List<UserDto> getAllUsers() {
//        List<User> users= userRepository.findAll();
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> UserMapper.toUserDto(user)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public UserDto updateUser(Long user_id, UserDto updateUserDto) {
        User user = userRepository.findById(Math.toIntExact(user_id)).orElseThrow(() -> new ResouceNotFound("id", user_id, "No user found/ exixts"));
        user.setId(updateUserDto.getUser_id());
        user.setFirst_name(updateUserDto.getFirst_name());
        user.setLast_name(updateUserDto.getLast_name());
        user.setEmail(updateUserDto.getEmail());
        user.setPassword(updateUserDto.getPassword());
        user.setRole(updateUserDto.getRole());
        user.setPhone_number(updateUserDto.getPhone_number());
        user.setAddress(updateUserDto.getAddress());
        user.setBalance(updateUserDto.getBalance());
        user.setAccountNumber(updateUserDto.getAccount_number());
        user.setCreate_at(updateUserDto.getCreate_at());
        user.setUpdate_at(updateUserDto.getUpdate_at());
        user.setDeleted_at(updateUserDto.getDeleted_at());
        User updatedUserData =  userRepository.save(user);
        return UserMapper.toUserDto(updatedUserData);
    }

    @Override
    public void deleteUser(Long user_id) {
        User user = userRepository.findById(Math.toIntExact(user_id)).orElseThrow(()-> new ResouceNotFound("id", user_id, "User"));
//        return UserMapper.toUserDto(user);
        userRepository.deleteById(Math.toIntExact(user_id));
    }

    @Override
    public Long UserCount(){
//        return userRepository.findAll().stream().count();
        return userRepository.count();
    }
}
