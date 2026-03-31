package com.jeevan.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    
    private UUID userId;
    private String username;
    private String email;
    private String role;
    private Boolean isAccountHolderActive;
    private LocalDateTime createdAt;
}
