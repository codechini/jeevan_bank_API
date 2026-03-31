package com.jeevan.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    
    private Long transactionId;
    private UUID accountId;
    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;
    private String relatedAccountNumber;
    private String status;
}
