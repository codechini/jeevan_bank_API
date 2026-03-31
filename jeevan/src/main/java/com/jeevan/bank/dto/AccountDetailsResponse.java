package com.jeevan.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsResponse {
    
    private UUID accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private String status;
    private LocalDateTime createdAt;
    
    private String holderFirstName;
    private String holderLastName;
    private String holderEmail;
    private String holderPhone;
    private String holderAddress;
    
    private List<TransactionSummary> recentTransactions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionSummary {
        private Long transactionId;
        private String transactionType;
        private BigDecimal amount;
        private LocalDateTime timestamp;
        private String description;
    }
}
