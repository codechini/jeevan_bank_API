package com.jeevanBank.jeevan.repository;

import com.jeevanBank.jeevan.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
//    List<Transactions> findByAccount_Account_idOrderByTimestampDesc(Long accountId);
    List<Transactions> findByAccount_IdOrderByTimestampDesc(Long accountId);

}
