//package com.jeevanBank.jeevan.entity;
//
//import jakarta.persistence.*;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//public class Documents {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long document_id;
//
//    @Column(nullable = false, unique = true)
//    private String document_name;
//
//    @Column(nullable = false, unique = true)
//    private String document_code;
//
//    @Column(nullable = false, unique = true)
//    private String document_type;
//
//    @Lob
//    @Column(nullable = false)
//    private String file;
//
//    @Column(nullable = false)
//    private Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());
//}
