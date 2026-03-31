package com.jeevan.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeBookResponse {
    
    private Long requestId;
    private String accountId;
    private String accountNumber;
    private LocalDateTime requestDate;
    private Integer numberOfLeaves;
    private String deliveryAddress;
    private String status;
    private String holderName;
}
