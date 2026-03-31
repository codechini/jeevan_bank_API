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
@Table(name = "\"User\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name ="\"user_id\"", updatable = false, nullable = false)
    private UUID userId;
    
    @Column(name = "\"username\"", nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(name = "\"password_hash\"", nullable = false)
    private String passwordHash;
    
    @Column(name = "\"email\"", nullable = false, unique = true, length = 150)
    private String email;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"role_id\"", nullable = false)
    private Role role;
    
    @CreationTimestamp
    @Column(name = "\"created_at\"", updatable = false)
    private LocalDateTime createdAt;
}
