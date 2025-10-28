package com.jeevanBank.jeevan.controller;

import com.jeevanBank.jeevan.dto.UserDto;
import com.jeevanBank.jeevan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:8082")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8082"})
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto result = userService.createUser(userDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> UserCount() {
//        Long result = userService.UserCount();
//        return new ResponseEntity<>(result, HttpStatus.OK);
        return ResponseEntity.ok(userService.UserCount());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> result = userService.getAllUsers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long user_id) {
        UserDto result = userService.getUserById(user_id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long user_id, @RequestBody UserDto userDto) {
        UserDto result = userService.updateUser(user_id, userDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long user_id) {
        userService.deleteUser(user_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
