package com.jeevan.bank.service;

import com.jeevan.bank.dto.UpdateProfileRequest;
import com.jeevan.bank.dto.UserDetailsResponse;
import com.jeevan.bank.dto.UserListResponse;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.Role;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.RoleRepository;
import com.jeevan.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final RoleRepository roleRepository;
    
    public List<UserListResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserListResponse)
                .collect(Collectors.toList());
    }
    
    public UserDetailsResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return mapToUserDetailsResponse(user);
    }
    
    @Transactional
    public UserDetailsResponse deactivateUser(UUID userId, UUID adminUserId) {
        if (userId.equals(adminUserId)) {
            throw new IllegalArgumentException("Cannot deactivate your own account");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElse(null);
        
        if (holder != null) {
            holder.setIsActive(false);
            accountHolderRepository.save(holder);
        }
        
        return mapToUserDetailsResponse(user);
    }
    
    @Transactional
    public UserDetailsResponse activateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElse(null);
        
        if (holder != null) {
            holder.setIsActive(true);
            accountHolderRepository.save(holder);
        }
        
        return mapToUserDetailsResponse(user);
    }
    
    @Transactional
    public UserDetailsResponse changeRole(UUID userId, String newRole, UUID adminUserId) {
        if (userId.equals(adminUserId)) {
            throw new IllegalArgumentException("Cannot change your own role");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Role role = roleRepository.findByRoleName(newRole.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + newRole));
        
        user.setRole(role);
        user = userRepository.save(user);
        
        return mapToUserDetailsResponse(user);
    }
    
    @Transactional
    public UserDetailsResponse adminUpdateProfile(UUID targetUserId, UpdateProfileRequest request) {
        return updateProfile(targetUserId, request);
    }

    @Transactional
    public UserDetailsResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        userRepository.save(user);

        if (request.getFirstName() != null || request.getLastName() != null) {
            accountHolderRepository.findByUser_UserId(userId).ifPresent(holder -> {
                if (request.getFirstName() != null) {
                    holder.setFirstName(request.getFirstName());
                }
                if (request.getLastName() != null) {
                    holder.setLastName(request.getLastName());
                }
                accountHolderRepository.save(holder);
            });
        }

        return mapToUserDetailsResponse(user);
    }

    private UserListResponse mapToUserListResponse(User user) {
        Boolean isActive = null;
        try {
            AccountHolder holder = accountHolderRepository.findByUser_UserId(user.getUserId())
                    .orElse(null);
            if (holder != null) {
                isActive = holder.getIsActive();
            }
        } catch (Exception e) {
            // Ignore
        }
        
        return UserListResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .isAccountHolderActive(isActive)
                .createdAt(user.getCreatedAt())
                .build();
    }
    
    private UserDetailsResponse mapToUserDetailsResponse(User user) {
        AccountHolder holder = null;
        try {
            holder = accountHolderRepository.findByUser_UserId(user.getUserId())
                    .orElse(null);
        } catch (Exception e) {
            // Ignore
        }
        
        List<UserDetailsResponse.AccountSummary> accounts = null;
        if (holder != null && holder.getAccounts() != null) {
            accounts = holder.getAccounts().stream()
                    .map(a -> UserDetailsResponse.AccountSummary.builder()
                            .accountId(a.getAccountId())
                            .accountNumber(a.getAccountNumber())
                            .accountType(a.getAccountType())
                            .status(a.getStatus())
                            .build())
                    .collect(Collectors.toList());
        }
        
        return UserDetailsResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .createdAt(user.getCreatedAt())
                .holderId(holder != null ? holder.getHolderId() : null)
                .firstName(holder != null ? holder.getFirstName() : null)
                .lastName(holder != null ? holder.getLastName() : null)
                .dateOfBirth(holder != null ? holder.getDateOfBirth() : null)
                .phone(holder != null ? holder.getPhone() : null)
                .address(holder != null ? holder.getAddress() : null)
                .citizenshipId(holder != null ? holder.getCitizenshipId() : null)
                .isActive(holder != null ? holder.getIsActive() : null)
                .accounts(accounts)
                .build();
    }
}
