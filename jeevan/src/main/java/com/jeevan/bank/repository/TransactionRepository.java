package com.jeevan.bank.repository;

import com.jeevan.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountAccountIdOrderByTimestampDesc(UUID accountId);
    List<Transaction> findByAccountAccountNumberOrderByTimestampDesc(String accountNumber);
}
