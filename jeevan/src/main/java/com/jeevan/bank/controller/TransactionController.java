package com.jeevan.bank.controller;

import com.jeevan.bank.dto.*;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.UserRepository;
import com.jeevan.bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/accounts/{accountId}")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByAccountId(
                accountId, user.getUserId(), false);
        
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }
    
    @GetMapping("/transactions/paginated")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getTransactionsPaginated(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Page<TransactionResponse> transactions = transactionService.getTransactionsByAccountIdPaginated(
                accountId, user.getUserId(), false, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }
    
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @PathVariable UUID accountId,
            @Valid @RequestBody DepositRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        TransactionResponse transaction = transactionService.deposit(
                accountId, request.getAmount(), request.getDescription(), user.getUserId(), false);
        
        return ResponseEntity.ok(ApiResponse.success("Deposit successful", transaction));
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @PathVariable UUID accountId,
            @Valid @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        TransactionResponse transaction = transactionService.withdraw(
                accountId, request.getAmount(), request.getDescription(), user.getUserId(), false);
        
        return ResponseEntity.ok(ApiResponse.success("Withdrawal successful", transaction));
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @PathVariable UUID accountId,
            @Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        TransactionResponse transaction = transactionService.transfer(
                accountId, request.getToAccountNumber(), request.getAmount(), 
                request.getDescription(), user.getUserId(), false);
        
        return ResponseEntity.ok(ApiResponse.success("Transfer successful", transaction));
    }
}
