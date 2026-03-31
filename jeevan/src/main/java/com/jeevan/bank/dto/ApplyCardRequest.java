package com.jeevan.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCardRequest {
    
    @NotNull(message = "Account ID is required")
    private UUID accountId;
    
    @NotBlank(message = "Card type is required")
    @Pattern(regexp = "^(CREDIT|DEBIT)$", message = "Card type must be CREDIT or DEBIT")
    private String cardType;
}
