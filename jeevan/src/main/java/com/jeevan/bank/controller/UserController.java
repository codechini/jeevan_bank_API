package com.jeevan.bank.controller;

import com.jeevan.bank.dto.*;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.UserRepository;
import com.jeevan.bank.service.AuthService;
import com.jeevan.bank.service.CardService;
import com.jeevan.bank.service.ChequeBookService;
import com.jeevan.bank.service.LoanService;
import com.jeevan.bank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LoanService loanService;
    private final ChequeBookService chequeBookService;
    private final CardService cardService;
    
    @PostMapping("/openaccount")
    public ResponseEntity<ApiResponse<AccountResponse>> openAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OpenAccountRequest request) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountResponse response = authService.openAccount(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Account opened successfully", response));
    }
    
    @PostMapping("/applyloan")
    public ResponseEntity<ApiResponse<LoanResponse>> applyLoan(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ApplyLoanRequest request) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        LoanResponse response = loanService.applyLoan(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Loan application submitted successfully", response));
    }
    
    @GetMapping("/viewloan")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> viewLoans(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<LoanResponse> loans = loanService.getLoansByUserId(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Loans retrieved successfully", loans));
    }
    
    @GetMapping("/viewchequebook")
    public ResponseEntity<ApiResponse<List<ChequeBookResponse>>> viewChequeBooks(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<ChequeBookResponse> chequeBooks = chequeBookService.getChequeBooksByUserId(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("ChequeBook requests retrieved successfully", chequeBooks));
    }
    
    @PostMapping("/applychequebook")
    public ResponseEntity<ApiResponse<ChequeBookResponse>> applyChequeBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ApplyChequeBookRequest request) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        ChequeBookResponse response = chequeBookService.applyChequeBook(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("ChequeBook request submitted successfully", response));
    }
    
    @GetMapping("/viewcard")
    public ResponseEntity<ApiResponse<List<CardResponse>>> viewCards(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<CardResponse> cards = cardService.getCardsByUserId(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Cards retrieved successfully", cards));
    }
    
    @PostMapping("/applycard")
    public ResponseEntity<ApiResponse<CardResponse>> applyCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ApplyCardRequest request) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        CardResponse response = cardService.applyCard(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Card application submitted successfully", response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetailsResponse response = userService.updateProfile(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }
}
