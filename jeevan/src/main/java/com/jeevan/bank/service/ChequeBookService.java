package com.jeevan.bank.service;

import com.jeevan.bank.dto.ApplyChequeBookRequest;
import com.jeevan.bank.dto.ChequeBookResponse;
import com.jeevan.bank.entity.Account;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.ChequeBookRequest;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.AccountRepository;
import com.jeevan.bank.repository.ChequeBookRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChequeBookService {
    
    private final ChequeBookRequestRepository chequeBookRequestRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    
    @Transactional
    public ChequeBookResponse applyChequeBook(UUID userId, ApplyChequeBookRequest request) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
            throw new IllegalArgumentException("Access denied: You don't own this account");
        }
        
        if (!"Active".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalArgumentException("Account is not active");
        }
        
        if (chequeBookRequestRepository.existsByAccountAccountIdAndStatus(request.getAccountId(), "Pending")) {
            throw new IllegalArgumentException("A pending chequebook request already exists for this account");
        }
        
        Integer numberOfLeaves = request.getNumberOfLeaves() != null ? request.getNumberOfLeaves() : 50;
        
        ChequeBookRequest chequeBookRequest = ChequeBookRequest.builder()
                .account(account)
                .numberOfLeaves(numberOfLeaves)
                .deliveryAddress(request.getDeliveryAddress())
                .status("Pending")
                .build();
        
        chequeBookRequest = chequeBookRequestRepository.save(chequeBookRequest);
        
        return mapToChequeBookResponse(chequeBookRequest);
    }
    
    public List<ChequeBookResponse> getChequeBooksByUserId(UUID userId) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        return chequeBookRequestRepository.findByAccountHolderHolderId(holder.getHolderId())
                .stream()
                .map(this::mapToChequeBookResponse)
                .collect(Collectors.toList());
    }
    
    public List<ChequeBookResponse> getAllChequeBooks() {
        return chequeBookRequestRepository.findAll()
                .stream()
                .map(this::mapToChequeBookResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ChequeBookResponse approveChequeBook(Long requestId) {
        ChequeBookRequest request = chequeBookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("ChequeBook request not found"));
        
        if (!"Pending".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalArgumentException("Only pending requests can be approved");
        }
        
        request.setStatus("Approved");
        request = chequeBookRequestRepository.save(request);
        
        return mapToChequeBookResponse(request);
    }
    
    @Transactional
    public ChequeBookResponse rejectChequeBook(Long requestId, String reason) {
        ChequeBookRequest request = chequeBookRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("ChequeBook request not found"));
        
        if (!"Pending".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalArgumentException("Only pending requests can be rejected");
        }
        
        request.setStatus("Rejected");
        request.setDeliveryAddress(reason);
        request = chequeBookRequestRepository.save(request);
        
        return mapToChequeBookResponse(request);
    }
    
    private ChequeBookResponse mapToChequeBookResponse(ChequeBookRequest request) {
        AccountHolder holder = request.getAccount().getHolder();
        return ChequeBookResponse.builder()
                .requestId(request.getRequestId())
                .accountId(request.getAccount().getAccountId().toString())
                .accountNumber(request.getAccount().getAccountNumber())
                .requestDate(request.getRequestDate())
                .numberOfLeaves(request.getNumberOfLeaves())
                .deliveryAddress(request.getDeliveryAddress())
                .status(request.getStatus())
                .holderName(holder.getFirstName() + " " + holder.getLastName())
                .build();
    }
}
