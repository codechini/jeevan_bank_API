package com.jeevan.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    
    private UUID cardId;
    private UUID accountId;
    private String accountNumber;
    private String cardNumber;
    private String cvv;
    private String cardType;
    private LocalDate expirationDate;
    private BigDecimal dailyLimit;
    private String status;
    private LocalDateTime issueDate;
    private String holderName;
}
