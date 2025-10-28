package com.jeevanBank.jeevan.service;

import com.jeevanBank.jeevan.dto.AccountDto;
import com.jeevanBank.jeevan.entity.Accounts;

import java.util.List;

public interface AccountService {
    Accounts createAccountForUser(Long userId) throws Exception;
    AccountDto getAccountDtoByUserId(Long userId);
    AccountDto getAccountDtoByAccountNumber(String accountNumber);
    Double getBalanceByAccountId(Long accountId);
    AccountDto deposit(Long accountId, Double amount);
    AccountDto withdraw(Long accountId, Double amount) throws Exception;
    List<?> getTransactions(Long accountId);
}
