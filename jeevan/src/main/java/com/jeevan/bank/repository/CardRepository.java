package com.jeevan.bank.repository;

import com.jeevan.bank.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    
    List<Card> findByAccountAccountId(UUID accountId);
    
    long countByAccountAccountId(UUID accountId);
    
    Optional<Card> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    @Query("SELECT c FROM Card c WHERE c.account.holder.holderId = :holderId")
    List<Card> findByAccountHolderHolderId(@Param("holderId") UUID holderId);
}
