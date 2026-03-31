package com.jeevan.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
    
    private UUID userId;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    
    private UUID holderId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private String citizenshipId;
    private Boolean isActive;
    
    private List<AccountSummary> accounts;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountSummary {
        private UUID accountId;
        private String accountNumber;
        private String accountType;
        private String status;
    }
}
