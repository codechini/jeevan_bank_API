package com.jeevan.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "AccountHolder")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"holder_id\"", updatable = false, nullable = false)
    private UUID holderId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"user_id\"", nullable = false, unique = true)
    private User user;
    
    @Column(name = "\"first_name\"", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "citizenship_id", unique = true, length = 50)
    private String citizenshipId;
    
    @Column(name = "ssn_hash", length = 100)
    private String ssnHash;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();
}
