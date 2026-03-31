package com.jeevan.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Card")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "card_id", updatable = false, nullable = false)
    private UUID cardId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;
    
    @Column(name = "card_type", nullable = false, length = 20)
    private String cardType;
    
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
    
    @Column(name = "cvv_hash", length = 100)
    private String cvvHash;
    
    @Column(name = "daily_limit", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal dailyLimit = new BigDecimal("500.00");
    
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "Active";
    
    @Column(name = "issue_date")
    @Builder.Default
    private LocalDate issueDate = LocalDate.now();
}
