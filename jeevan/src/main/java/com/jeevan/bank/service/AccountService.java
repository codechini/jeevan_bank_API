package com.jeevan.bank.service;

import com.jeevan.bank.dto.AdminUpdateAccountRequest;
import com.jeevan.bank.dto.AccountDetailsResponse;
import com.jeevan.bank.dto.AccountListResponse;
import com.jeevan.bank.entity.Account;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.AccountRepository;
import com.jeevan.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final UserRepository userRepository;
    
    public List<AccountListResponse> getAccountsByUserId(UUID userId) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        return accountRepository.findByHolderHolderId(holder.getHolderId())
                .stream()
                .map(this::mapToAccountListResponse)
                .collect(Collectors.toList());
    }
    
    public List<AccountListResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::mapToAccountListResponse)
                .collect(Collectors.toList());
    }
    
    public List<AccountListResponse> getAccountsByHolderId(UUID holderId) {
        return accountRepository.findByHolderHolderId(holderId)
                .stream()
                .map(this::mapToAccountListResponse)
                .collect(Collectors.toList());
    }
    
    public AccountDetailsResponse getAccountById(UUID accountId, UUID userId, boolean isAdmin) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!isAdmin) {
            AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
            
            if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
                throw new IllegalArgumentException("You do not have permission to view this account");
            }
        }
        
        return mapToAccountDetailsResponse(account);
    }
    
    @Transactional
    public AccountListResponse closeAccount(UUID accountId, UUID userId, boolean isAdmin) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!isAdmin) {
            AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
            
            if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
                throw new IllegalArgumentException("You do not have permission to close this account");
            }
        }
        
        if ("Closed".equalsIgnoreCase(account.getStatus()) || "Inactive".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalArgumentException("Account is already closed");
        }
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot close account with negative balance");
        }
        
        account.setStatus("Closed");
        account = accountRepository.save(account);
        
        return mapToAccountListResponse(account);
    }
    
    @Transactional
    public AccountDetailsResponse updateAccount(UUID accountId, UUID adminId, AdminUpdateAccountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        AccountHolder holder = account.getHolder();
        
        if (request.getAccountType() != null) {
            account.setAccountType(request.getAccountType());
            account = accountRepository.save(account);
        }
        
        if (request.getFirstName() != null) {
            holder.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            holder.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null) {
            holder.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            holder.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            holder.setPhone(request.getPhone());
        }
        if (request.getCitizenshipId() != null) {
            holder.setCitizenshipId(request.getCitizenshipId());
        }
        
        accountHolderRepository.save(holder);
        
        return mapToAccountDetailsResponse(account);
    }
    
    @Transactional
    public AccountDetailsResponse updateAccountByUser(UUID accountId, UUID userId, AdminUpdateAccountRequest request) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
            throw new IllegalArgumentException("You do not have permission to update this account");
        }
        
        if (request.getAccountType() != null) {
            account.setAccountType(request.getAccountType());
            account = accountRepository.save(account);
        }
        
        if (request.getFirstName() != null) {
            holder.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            holder.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null) {
            holder.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            holder.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            holder.setPhone(request.getPhone());
        }
        if (request.getCitizenshipId() != null) {
            holder.setCitizenshipId(request.getCitizenshipId());
        }
        
        accountHolderRepository.save(holder);
        
        return mapToAccountDetailsResponse(account);
    }
    
    @Transactional
    public void deleteAccount(UUID accountId, UUID userId, boolean isAdmin) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!isAdmin) {
            AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
            
            if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
                throw new IllegalArgumentException("You do not have permission to delete this account");
            }
        }
        
        if (!"Closed".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalArgumentException("Only closed accounts can be deleted");
        }
        
        if (!isAdmin && account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Cannot delete account with non-zero balance");
        }
        
        accountRepository.delete(account);
    }
    
    private AccountListResponse mapToAccountListResponse(Account account) {
        return AccountListResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .holderName(account.getHolder().getFirstName() + " " + account.getHolder().getLastName())
                .createdAt(account.getCreatedAt())
                .build();
    }
    
    private AccountDetailsResponse mapToAccountDetailsResponse(Account account) {
        AccountHolder holder = account.getHolder();
        User user = holder.getUser();
        
        List<AccountDetailsResponse.TransactionSummary> recentTransactions = account.getTransactions()
                .stream()
                .limit(10)
                .map(t -> AccountDetailsResponse.TransactionSummary.builder()
                        .transactionId(t.getTransactionId())
                        .transactionType(t.getTransactionType())
                        .amount(t.getAmount())
                        .timestamp(t.getTimestamp())
                        .description(t.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        return AccountDetailsResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .interestRate(account.getInterestRate())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .holderFirstName(holder.getFirstName())
                .holderLastName(holder.getLastName())
                .holderEmail(user.getEmail())
                .holderPhone(holder.getPhone())
                .holderAddress(holder.getAddress())
                .recentTransactions(recentTransactions)
                .build();
    }
}
