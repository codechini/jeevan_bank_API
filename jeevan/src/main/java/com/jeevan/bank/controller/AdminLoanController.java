package com.jeevan.bank.controller;

import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.dto.LoanResponse;
import com.jeevan.bank.dto.RejectLoanRequest;
import com.jeevan.bank.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/loans")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLoanController {
    
    private final LoanService loanService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getAllLoans() {
        List<LoanResponse> loans = loanService.getAllLoans();
        return ResponseEntity.ok(ApiResponse.success("All loans retrieved successfully", loans));
    }
    
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<ApiResponse<LoanResponse>> approveLoan(@PathVariable UUID loanId) {
        LoanResponse loan = loanService.approveLoan(loanId);
        return ResponseEntity.ok(ApiResponse.success("Loan approved successfully", loan));
    }
    
    @PostMapping("/{loanId}/reject")
    public ResponseEntity<ApiResponse<LoanResponse>> rejectLoan(
            @PathVariable UUID loanId,
            @Valid @RequestBody RejectLoanRequest request) {
        LoanResponse loan = loanService.rejectLoan(loanId, request);
        return ResponseEntity.ok(ApiResponse.success("Loan rejected successfully", loan));
    }
}
