package com.jeevanBank.jeevan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Accounts account;

    @Column(nullable = false)
    private String type; // DEPOSIT / WITHDRAW

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = true)
    private Double balanceAfter;

    @Column(nullable = false)
    private Timestamp timestamp;
}
