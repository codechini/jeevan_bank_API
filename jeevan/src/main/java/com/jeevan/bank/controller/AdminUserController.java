package com.jeevan.bank.controller;

import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.dto.RoleChangeRequest;
import com.jeevan.bank.dto.UserDetailsResponse;
import com.jeevan.bank.dto.UserListResponse;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.UserRepository;
import com.jeevan.bank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    
    private final UserService userService;
    private final UserRepository userRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserListResponse>>> getAllUsers() {
        List<UserListResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUser(
            @PathVariable UUID userId) {
        UserDetailsResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", user));
    }
    
    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> deactivateUser(
            @PathVariable UUID userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        UserDetailsResponse user = userService.deactivateUser(userId, admin.getUserId());
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", user));
    }
    
    @PutMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> activateUser(
            @PathVariable UUID userId) {
        UserDetailsResponse user = userService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", user));
    }
    
    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> changeRole(
            @PathVariable UUID userId,
            @Valid @RequestBody RoleChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        UserDetailsResponse user = userService.changeRole(userId, request.getRole(), admin.getUserId());
        return ResponseEntity.ok(ApiResponse.success("User role updated successfully", user));
    }
}
