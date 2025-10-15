package com.jeevanBank.jeevan.entity;

import com.jeevanBank.jeevan.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;


import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, unique = true)
    private String phone_number;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false, unique = true)
    private String account_number;

    @CreationTimestamp
    @Column(nullable = true)
//    private Timestamp create_at;
    private Timestamp create_at;


    @UpdateTimestamp
    @Column(nullable = true)
    private Timestamp update_at;

    @Column(nullable = true)
    private Timestamp deleted_at;

}
