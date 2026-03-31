package com.jeevan.bank.service;

import com.jeevan.bank.dto.TransactionResponse;
import com.jeevan.bank.entity.Account;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.Transaction;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.AccountRepository;
import com.jeevan.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    
    public List<TransactionResponse> getTransactionsByAccountId(UUID accountId, UUID userId, boolean isAdmin) {
        Account account = validateAccountAccess(accountId, userId, isAdmin);
        
        return transactionRepository.findByAccountAccountIdOrderByTimestampDesc(account.getAccountId())
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }
    
    public Page<TransactionResponse> getTransactionsByAccountIdPaginated(UUID accountId, UUID userId, boolean isAdmin, Pageable pageable) {
        Account account = validateAccountAccess(accountId, userId, isAdmin);
        
        List<Transaction> transactions = transactionRepository.findByAccountAccountIdOrderByTimestampDesc(account.getAccountId());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), transactions.size());
        
        List<TransactionResponse> pageContent;
        if (start < transactions.size()) {
            pageContent = transactions.subList(start, end).stream()
                    .map(this::mapToTransactionResponse)
                    .collect(Collectors.toList());
        } else {
            pageContent = List.of();
        }
        
        return new PageImpl<>(pageContent, pageable, transactions.size());
    }
    
    @Transactional
    public TransactionResponse deposit(UUID accountId, BigDecimal amount, String description, UUID userId, boolean isAdmin) {
        Account account = validateAccountAccess(accountId, userId, isAdmin);
        
        validateAccountStatus(account);
        
        account.setBalance(account.getBalance().add(amount));
        account = accountRepository.save(account);
        
        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionType("DEPOSIT")
                .amount(amount)
                .description(description != null ? description : "Deposit")
                .status("Completed")
                .build();
        
        transaction = transactionRepository.save(transaction);
        
        return mapToTransactionResponse(transaction);
    }
    
    @Transactional
    public TransactionResponse withdraw(UUID accountId, BigDecimal amount, String description, UUID userId, boolean isAdmin) {
        Account account = validateAccountAccess(accountId, userId, isAdmin);
        
        validateAccountStatus(account);
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        account = accountRepository.save(account);
        
        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionType("WITHDRAW")
                .amount(amount)
                .description(description != null ? description : "Withdrawal")
                .status("Completed")
                .build();
        
        transaction = transactionRepository.save(transaction);
        
        return mapToTransactionResponse(transaction);
    }
    
    @Transactional
    public TransactionResponse transfer(UUID fromAccountId, String toAccountNumber, BigDecimal amount, String description, UUID userId, boolean isAdmin) {
        Account fromAccount = validateAccountAccess(fromAccountId, userId, isAdmin);
        
        validateAccountStatus(fromAccount);
        
        if (fromAccount.getAccountNumber().equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));
        
        if (!"Active".equalsIgnoreCase(toAccount.getStatus())) {
            throw new IllegalArgumentException("Destination account is not active");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        Transaction debitTransaction = Transaction.builder()
                .account(fromAccount)
                .transactionType("TRANSFER")
                .amount(amount.negate())
                .description(description != null ? description : "Transfer to " + toAccountNumber)
                .relatedAccountNumber(toAccountNumber)
                .status("Completed")
                .build();
        
        Transaction creditTransaction = Transaction.builder()
                .account(toAccount)
                .transactionType("TRANSFER")
                .amount(amount)
                .description(description != null ? description : "Transfer from " + fromAccount.getAccountNumber())
                .relatedAccountNumber(fromAccount.getAccountNumber())
                .status("Completed")
                .build();
        
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        
        return mapToTransactionResponse(debitTransaction);
    }
    
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }
    
    public List<TransactionResponse> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountAccountNumberOrderByTimestampDesc(accountNumber)
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }
    
    private Account validateAccountAccess(UUID accountId, UUID userId, boolean isAdmin) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!isAdmin) {
            AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
            
            if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
                throw new IllegalArgumentException("You do not have permission to access this account");
            }
        }
        
        return account;
    }
    
    private void validateAccountStatus(Account account) {
        if (!"Active".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalArgumentException("Account is not active");
        }
    }
    
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .accountId(transaction.getAccount().getAccountId())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .description(transaction.getDescription())
                .relatedAccountNumber(transaction.getRelatedAccountNumber())
                .status(transaction.getStatus())
                .build();
    }
}
