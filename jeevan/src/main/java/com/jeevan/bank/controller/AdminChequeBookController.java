package com.jeevan.bank.controller;

import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.dto.ChequeBookResponse;
import com.jeevan.bank.service.ChequeBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/chequebooks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminChequeBookController {
    
    private final ChequeBookService chequeBookService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChequeBookResponse>>> getAllChequeBooks() {
        List<ChequeBookResponse> chequeBooks = chequeBookService.getAllChequeBooks();
        return ResponseEntity.ok(ApiResponse.success("All chequebook requests retrieved successfully", chequeBooks));
    }
    
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<ChequeBookResponse>> approveChequeBook(@PathVariable Long requestId) {
        ChequeBookResponse chequeBook = chequeBookService.approveChequeBook(requestId);
        return ResponseEntity.ok(ApiResponse.success("ChequeBook request approved successfully", chequeBook));
    }
    
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<ChequeBookResponse>> rejectChequeBook(
            @PathVariable Long requestId,
            @RequestParam(required = false, defaultValue = "Rejected by admin") String reason) {
        ChequeBookResponse chequeBook = chequeBookService.rejectChequeBook(requestId, reason);
        return ResponseEntity.ok(ApiResponse.success("ChequeBook request rejected successfully", chequeBook));
    }
}
