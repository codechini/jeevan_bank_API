package com.jeevan.bank.repository;

import com.jeevan.bank.entity.ChequeBookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChequeBookRequestRepository extends JpaRepository<ChequeBookRequest, Long> {
    List<ChequeBookRequest> findByAccountHolderHolderId(UUID holderId);
    
    boolean existsByAccountAccountIdAndStatus(UUID accountId, String status);
}
