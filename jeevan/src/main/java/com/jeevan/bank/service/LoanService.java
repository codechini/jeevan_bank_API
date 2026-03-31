package com.jeevan.bank.service;

import com.jeevan.bank.dto.ApplyLoanRequest;
import com.jeevan.bank.dto.LoanResponse;
import com.jeevan.bank.dto.RejectLoanRequest;
import com.jeevan.bank.entity.Account;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.Loan;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.AccountRepository;
import com.jeevan.bank.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {
    
    private static final Set<String> VALID_LOAN_TYPES = Set.of("PERSONAL", "HOME", "AUTO", "EDUCATION");
    
    private static final BigDecimal PERSONAL_RATE = new BigDecimal("0.12");
    private static final BigDecimal HOME_RATE = new BigDecimal("0.085");
    private static final BigDecimal AUTO_RATE = new BigDecimal("0.10");
    private static final BigDecimal EDUCATION_RATE = new BigDecimal("0.06");
    
    private final LoanRepository loanRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    @Autowired
    private TransactionService transactionService;
    
    @Transactional
    public LoanResponse applyLoan(UUID userId, ApplyLoanRequest request) {
        String loanType = request.getLoanType().toUpperCase();
        
        if (!VALID_LOAN_TYPES.contains(loanType)) {
            throw new IllegalArgumentException(
                    "Invalid loan type. Must be one of: " + String.join(", ", VALID_LOAN_TYPES));
        }
        
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        BigDecimal interestRate = getInterestRate(loanType);
        
        Loan loan = Loan.builder()
                .holder(holder)
                .loanType(loanType)
                .principalAmount(request.getPrincipalAmount())
                .currentBalance(request.getPrincipalAmount())
                .interestRate(interestRate)
                .termMonths(request.getTermMonths())
                .startDate(LocalDate.now())
                .status("Pending")
                .reason(request.getReason())
                .build();
        
        loan = loanRepository.save(loan);
        
        return mapToLoanResponse(loan);
    }
    
    public List<LoanResponse> getLoansByUserId(UUID userId) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        return loanRepository.findByHolderHolderId(holder.getHolderId())
                .stream()
                .map(this::mapToLoanResponse)
                .collect(Collectors.toList());
    }
    
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::mapToLoanResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public LoanResponse approveLoan(UUID loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        
        if (!"Pending".equalsIgnoreCase(loan.getStatus())) {
            throw new IllegalArgumentException("Only pending loans can be approved");
        }
        
        AccountHolder holder = loan.getHolder();
        Account primaryAccount = accountRepository.findByHolderHolderId(holder.getHolderId())
                .stream()
                .filter(acc -> "Active".equalsIgnoreCase(acc.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active account found for loan disbursement"));
        
        transactionService.deposit(
                primaryAccount.getAccountId(),
                loan.getPrincipalAmount(),
                "Loan disbursement - " + loan.getLoanType() + " loan approved",
                null,
                true
        );
        
        loan.setStatus("Approved");
        loan.setStartDate(LocalDate.now());
        loan = loanRepository.save(loan);
        
        return mapToLoanResponse(loan);
    }
    
    @Transactional
    public LoanResponse rejectLoan(UUID loanId, RejectLoanRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        
        if (!"Pending".equalsIgnoreCase(loan.getStatus())) {
            throw new IllegalArgumentException("Only pending loans can be rejected");
        }
        
        loan.setStatus("Rejected");
        loan.setReason(request.getReason());
        loan = loanRepository.save(loan);
        
        return mapToLoanResponse(loan);
    }
    
    private BigDecimal getInterestRate(String loanType) {
        return switch (loanType) {
            case "PERSONAL" -> PERSONAL_RATE;
            case "HOME" -> HOME_RATE;
            case "AUTO" -> AUTO_RATE;
            case "EDUCATION" -> EDUCATION_RATE;
            default -> PERSONAL_RATE;
        };
    }
    
    private LoanResponse mapToLoanResponse(Loan loan) {
        return LoanResponse.builder()
                .loanId(loan.getLoanId())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .currentBalance(loan.getCurrentBalance())
                .interestRate(loan.getInterestRate())
                .termMonths(loan.getTermMonths())
                .startDate(loan.getStartDate())
                .status(loan.getStatus())
                .reason(loan.getReason())
                .holderName(loan.getHolder().getFirstName() + " " + loan.getHolder().getLastName())
                .build();
    }
}
