package com.jeevanBank.jeevan.repository;

import com.jeevanBank.jeevan.entity.Accounts;
import com.jeevanBank.jeevan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long>{
    Optional<Accounts> findByAccountNumber(String accountNumber);
    Optional<Accounts> findByUser(User user);
}
