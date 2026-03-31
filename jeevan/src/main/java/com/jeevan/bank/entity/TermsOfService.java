package com.jeevan.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TermsOfService")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsOfService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_id")
    private Integer termId;
    
    @Column(name = "version_number", nullable = false, unique = true, length = 20)
    private String versionNumber;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "effective_date", nullable = false, unique = true)
    private LocalDate effectiveDate;
}
