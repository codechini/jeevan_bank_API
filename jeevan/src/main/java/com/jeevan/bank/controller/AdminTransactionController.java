package com.jeevan.bank.controller;

import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.dto.TransactionResponse;
import com.jeevan.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTransactionController {
    
    private final TransactionService transactionService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions(
            @RequestParam(required = false) String accountNumber) {
        
        List<TransactionResponse> transactions;
        if (accountNumber != null && !accountNumber.isEmpty()) {
            transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        } else {
            transactions = transactionService.getAllTransactions();
        }
        
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }
}
