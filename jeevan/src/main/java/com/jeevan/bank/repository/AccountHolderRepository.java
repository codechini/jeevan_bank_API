package com.jeevan.bank.repository;

import com.jeevan.bank.entity.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, UUID> {
    Optional<AccountHolder> findByUser_UserId(UUID userId);
    Optional<AccountHolder> findByCitizenshipId(String citizenshipId);
}
