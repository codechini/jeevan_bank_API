package com.jeevan.bank.repository;

import com.jeevan.bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByHolderHolderId(UUID holderId);
    boolean existsByAccountNumber(String accountNumber);
}
