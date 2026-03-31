package com.jeevan.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OpenAccountRequest {
    @NotBlank
    @Size(max = 100)
    private String firstName;
    
    @NotBlank
    @Size(max = 100)
    private String lastName;
    
    private LocalDate dateOfBirth;
    
    @Size(max = 500)
    private String address;
    
    @Pattern(regexp = "^[+]?[0-9]{10,20}$")
    private String phone;
    
    @Size(max = 50)
    private String citizenshipId;
    
    @NotBlank
    @Pattern(regexp = "^(SAVINGS|CHECKING|FIXED_DEPOSIT)$")
    private String accountType;
}
