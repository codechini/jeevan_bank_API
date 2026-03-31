package com.jeevan.bank.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminUpdateAccountRequest {
    
    @Pattern(regexp = "^(SAVINGS|CHECKING|FIXED_DEPOSIT)$", message = "Account type must be SAVINGS, CHECKING, or FIXED_DEPOSIT")
    private String accountType;
    
    @Size(max = 100)
    private String firstName;
    
    @Size(max = 100)
    private String lastName;
    
    private LocalDate dateOfBirth;
    
    @Size(max = 500)
    private String address;
    
    @Pattern(regexp = "^[+]?[0-9]{10,20}$", message = "Invalid phone number format")
    private String phone;
    
    @Size(max = 50)
    private String citizenshipId;
}
