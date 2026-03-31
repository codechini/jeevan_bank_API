package com.jeevan.bank.controller;

import com.jeevan.bank.dto.ApiResponse;
import com.jeevan.bank.dto.CardResponse;
import com.jeevan.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
    
    private final CardService cardService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<CardResponse>>> getAllCards() {
        List<CardResponse> cards = cardService.getAllCards();
        return ResponseEntity.ok(ApiResponse.success("All cards retrieved successfully", cards));
    }
    
    @PostMapping("/{cardId}/approve")
    public ResponseEntity<ApiResponse<CardResponse>> approveCard(@PathVariable UUID cardId) {
        CardResponse card = cardService.approveCard(cardId);
        return ResponseEntity.ok(ApiResponse.success("Card approved successfully", card));
    }
    
    @PostMapping("/{cardId}/reject")
    public ResponseEntity<ApiResponse<CardResponse>> rejectCard(
            @PathVariable UUID cardId,
            @RequestParam(required = false, defaultValue = "Rejected by admin") String reason) {
        CardResponse card = cardService.rejectCard(cardId, reason);
        return ResponseEntity.ok(ApiResponse.success("Card rejected successfully", card));
    }
}
