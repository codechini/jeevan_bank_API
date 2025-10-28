package com.jeevanBank.jeevan.service.impl;

import com.jeevanBank.jeevan.dto.AccountDto;
import com.jeevanBank.jeevan.entity.Accounts;
import com.jeevanBank.jeevan.entity.Transactions;
import com.jeevanBank.jeevan.entity.User;
import com.jeevanBank.jeevan.exception.ResouceNotFound;
import com.jeevanBank.jeevan.repository.AccountsRepository;
import com.jeevanBank.jeevan.repository.TransactionsRepository;
import com.jeevanBank.jeevan.repository.UserRepository;
import com.jeevanBank.jeevan.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountsRepository accountsRepository;
    private final UserRepository userRepository;
    private final TransactionsRepository transactionsRepository;

    public AccountServiceImpl(AccountsRepository accountsRepository,
                              UserRepository userRepository,
                              TransactionsRepository transactionsRepository) {
        this.accountsRepository = accountsRepository;
        this.userRepository = userRepository;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    @Transactional
    public Accounts createAccountForUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFound("User", userId, "id"));

        // if user already has account, return existing or throw
        accountsRepository.findByUser(user).ifPresent(a -> { throw new RuntimeException("Account already exists"); });

        Accounts account = Accounts.builder()
                .user(user)
                .accountNumber(user.getAccountNumber()) // copy from user
                .balance(user.getBalance())
                .build();

        Accounts saved = accountsRepository.save(account);
        // optionally set account in user (if you added field)
        // user.setAccount(saved);
        // userRepository.save(user);
        return saved;
    }

    @Override
    public AccountDto getAccountDtoByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFound("User", userId, "id"));
        Accounts acc = accountsRepository.findByUser(user)
                .orElseThrow(() -> new ResouceNotFound("Accounts", userId, "userId"));
        return toDto(acc);
    }

    @Override
    public AccountDto getAccountDtoByAccountNumber(String accountNumber) {
        Accounts acc = accountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResouceNotFound("Accounts", "account_number", accountNumber));
        return toDto(acc);
    }

    @Override
    public Double getBalanceByAccountId(Long accountId) {
        Accounts acc = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResouceNotFound("Accounts", accountId, "id"));
        return acc.getBalance();
    }

    @Override
    @Transactional
    public AccountDto deposit(Long accountId, Double amount) {
        Accounts acc = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResouceNotFound("Accounts", accountId, "id"));
        acc.setBalance(acc.getBalance() + amount);
        Accounts saved = accountsRepository.save(acc);

        // record transaction
        Transactions t = new Transactions();
        t.setAccount(saved);
        t.setAmount(amount);
        t.setType("DEPOSIT");
        t.setTimestamp(java.sql.Timestamp.from(Instant.now()));
        t.setBalanceAfter(saved.getBalance());
        transactionsRepository.save(t);

        return toDto(saved);
    }

    @Override
    @Transactional
    public AccountDto withdraw(Long accountId, Double amount) throws Exception {
        Accounts acc = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResouceNotFound("Accounts", accountId, "id"));

        if (acc.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        Accounts saved = accountsRepository.save(acc);

        // record transaction
        Transactions t = new Transactions();
        t.setAccount(saved);
        t.setAmount(amount);
        t.setType("WITHDRAW");
        t.setTimestamp(java.sql.Timestamp.from(Instant.now()));
        t.setBalanceAfter(saved.getBalance());
        transactionsRepository.save(t);

        return toDto(saved);
    }

    @Override
    public List<?> getTransactions(Long accountId) {
        return transactionsRepository.findByAccount_IdOrderByTimestampDesc(accountId);
    }

    private AccountDto toDto(Accounts a){
        return AccountDto.builder()
                .accountId(a.getId())
                .accountNumber(a.getAccountNumber())
                .balance(a.getBalance())
                .userId(a.getUser() != null ? a.getUser().getId() : null)
                .build();
    }
}
