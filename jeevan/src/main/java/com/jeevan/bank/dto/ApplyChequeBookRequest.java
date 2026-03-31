package com.jeevan.bank.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyChequeBookRequest {
    
    private UUID accountId;
    
    @Max(value = 50, message = "Number of leaves cannot exceed 50")
    private Integer numberOfLeaves;
    
    private String deliveryAddress;
}
