package com.jeevan.bank.controller;

import com.jeevan.bank.dto.AccountDetailsResponse;
import com.jeevan.bank.dto.AccountListResponse;
import com.jeevan.bank.dto.AccountResponse;
import com.jeevan.bank.dto.AdminOpenAccountRequest;
import com.jeevan.bank.dto.AdminUpdateAccountRequest;
import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.dto.DepositRequest;
import com.jeevan.bank.dto.TransactionResponse;
import com.jeevan.bank.dto.TransferRequest;
import com.jeevan.bank.dto.WithdrawRequest;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.UserRepository;
import com.jeevan.bank.service.AccountService;
import com.jeevan.bank.service.AuthService;
import com.jeevan.bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountController {
    
    private final AccountService accountService;
    private final AuthService authService;
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    
    @PostMapping("/open")
    public ResponseEntity<ApiResponse<AccountResponse>> openAccount(
            @Valid @RequestBody AdminOpenAccountRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        AccountResponse account = authService.openAccountByAdmin(admin.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Account opened successfully", account));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountListResponse>>> getAllAccounts(
            @RequestParam(required = false) UUID holderId) {
        
        List<AccountListResponse> accounts;
        if (holderId != null) {
            accounts = accountService.getAccountsByHolderId(holderId);
        } else {
            accounts = accountService.getAllAccounts();
        }
        return ResponseEntity.ok(ApiResponse.success("All accounts retrieved successfully", accounts));
    }
    
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> getAccount(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountDetailsResponse account = accountService.getAccountById(accountId, admin.getUserId(), true);
        return ResponseEntity.ok(ApiResponse.success("Account details retrieved successfully", account));
    }
    
    @PutMapping("/{accountId}/close")
    public ResponseEntity<ApiResponse<AccountListResponse>> closeAccount(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountListResponse account = accountService.closeAccount(accountId, admin.getUserId(), true);
        return ResponseEntity.ok(ApiResponse.success("Account closed successfully", account));
    }
    
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDetailsResponse>> updateAccount(
            @PathVariable UUID accountId,
            @Valid @RequestBody AdminUpdateAccountRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        AccountDetailsResponse account = accountService.updateAccount(accountId, admin.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Account updated successfully", account));
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> deleteAccount(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        accountService.deleteAccount(accountId, admin.getUserId(), true);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully", "Account deleted"));
    }
    
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @PathVariable UUID accountId,
            @Valid @RequestBody DepositRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        TransactionResponse transaction = transactionService.deposit(
                accountId, request.getAmount(), request.getDescription(), admin.getUserId(), true);
        
        return ResponseEntity.ok(ApiResponse.success("Deposit successful", transaction));
    }
    
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @PathVariable UUID accountId,
            @Valid @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        TransactionResponse transaction = transactionService.withdraw(
                accountId, request.getAmount(), request.getDescription(), admin.getUserId(), true);
        
        return ResponseEntity.ok(ApiResponse.success("Withdrawal successful", transaction));
    }
    
    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @PathVariable UUID accountId,
            @Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        
        TransactionResponse transaction = transactionService.transfer(
                accountId, request.getToAccountNumber(), request.getAmount(),
                request.getDescription(), admin.getUserId(), true);
        
        return ResponseEntity.ok(ApiResponse.success("Transfer successful", transaction));
    }
}
