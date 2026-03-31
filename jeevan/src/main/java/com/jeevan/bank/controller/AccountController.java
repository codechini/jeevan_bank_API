package com.jeevan.bank.controller;

import com.jeevan.bank.dto.AccountDetailsResponse;
import com.jeevan.bank.dto.AccountListResponse;
import com.jeevan.bank.dto.AdminUpdateAccountRequest;
import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.UserRepository;
import com.jeevan.bank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountService accountService;
    private final UserRepository userRepository;
    private final AccountHolderRepository accountHolderRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountListResponse>>> getMyAccounts(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<AccountListResponse> accounts = accountService.getAccountsByUserId(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
    }
    
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> getMyAccount(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountDetailsResponse account = accountService.getAccountById(accountId, user.getUserId(), false);
        return ResponseEntity.ok(ApiResponse.success("Account details retrieved successfully", account));
    }
    
    @PutMapping("/{accountId}/close")
    public ResponseEntity<ApiResponse<AccountListResponse>> closeMyAccount(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountListResponse account = accountService.closeAccount(accountId, user.getUserId(), false);
        return ResponseEntity.ok(ApiResponse.success("Account closed successfully", account));
    }
    
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> updateMyAccount(
            @PathVariable UUID accountId,
            @Valid @RequestBody AdminUpdateAccountRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountDetailsResponse account = accountService.updateAccountByUser(accountId, user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Account updated successfully", account));
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> deleteMyAccount(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        accountService.deleteAccount(accountId, user.getUserId(), false);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully", "Account deleted"));
    }
}
