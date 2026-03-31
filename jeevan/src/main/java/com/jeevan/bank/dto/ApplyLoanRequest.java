package com.jeevan.bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplyLoanRequest {
    
    @NotBlank(message = "Loan type is required")
    private String loanType;
    
    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "0.01", message = "Principal amount must be greater than 0")
    private BigDecimal principalAmount;
    
    @NotNull(message = "Term months is required")
    @Positive(message = "Term months must be positive")
    private Integer termMonths;
    
    private String reason;
}
