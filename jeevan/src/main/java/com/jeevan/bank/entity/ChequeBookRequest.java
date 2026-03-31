package com.jeevan.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ChequeBookRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeBookRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @CreationTimestamp
    @Column(name = "request_date", updatable = false)
    private LocalDateTime requestDate;
    
    @Column(name = "number_of_leaves", nullable = false)
    @Builder.Default
    private Integer numberOfLeaves = 50;
    
    @Column(name = "delivery_address", columnDefinition = "TEXT")
    private String deliveryAddress;
    
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "Pending";
}
