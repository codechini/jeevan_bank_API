//package com.jeevanBank.jeevan.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//import java.sql.Timestamp;
//
//public class Accounts {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long account_id;
//
//    @Column(nullable = false)
//    private String role;
//
//    @Column(nullable = false)
//    private Double balance;
//
//    @Column(nullable = false, unique = true)
//    private String account_number;
//
//    @Column(nullable = false)
//    private Timestamp create_at;
//
//    @Column(nullable = false)
//    private Timestamp update_at;
//
//    @Column(nullable = false)
//    private Timestamp deleted_at;
//}
