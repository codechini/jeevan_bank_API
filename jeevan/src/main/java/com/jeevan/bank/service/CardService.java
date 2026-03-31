package com.jeevan.bank.service;

import com.jeevan.bank.dto.ApplyCardRequest;
import com.jeevan.bank.dto.CardResponse;
import com.jeevan.bank.entity.Account;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.Card;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.AccountRepository;
import com.jeevan.bank.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    
    private static final int MAX_CARDS_PER_ACCOUNT = 3;
    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int CVV_LENGTH = 3;
    private static final int CARD_VALIDITY_YEARS = 3;
    
    private final CardRepository cardRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Transactional
    public CardResponse applyCard(UUID userId, ApplyCardRequest request) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!account.getHolder().getHolderId().equals(holder.getHolderId())) {
            throw new IllegalArgumentException("Access denied: You don't own this account");
        }
        
        if (!"Active".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalArgumentException("Account is not active");
        }
        
        long existingCardCount = cardRepository.countByAccountAccountId(request.getAccountId());
        if (existingCardCount >= MAX_CARDS_PER_ACCOUNT) {
            throw new IllegalArgumentException("Maximum card limit (" + MAX_CARDS_PER_ACCOUNT + ") reached for this account");
        }
        
        String cardNumber = generateUniqueCardNumber();
        String cvv = generateCVV();
        String cvvHash = passwordEncoder.encode(cvv);
        
        Card card = Card.builder()
                .account(account)
                .cardNumber(cardNumber)
                .cardType(request.getCardType())
                .expirationDate(LocalDate.now().plusYears(CARD_VALIDITY_YEARS))
                .cvvHash(cvvHash)
                .dailyLimit(new BigDecimal("500.00"))
                .status("Pending")
                .issueDate(LocalDate.now())
                .build();
        
        card = cardRepository.save(card);
        
        return mapToCardResponse(card, cvv);
    }
    
    public List<CardResponse> getCardsByUserId(UUID userId) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        return cardRepository.findByAccountHolderHolderId(holder.getHolderId())
                .stream()
                .map(card -> mapToCardResponse(card, null))
                .collect(Collectors.toList());
    }
    
    public List<CardResponse> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(card -> mapToCardResponse(card, null))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CardResponse approveCard(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        
        if (!"Pending".equalsIgnoreCase(card.getStatus())) {
            throw new IllegalArgumentException("Only pending cards can be approved");
        }
        
        card.setStatus("Active");
        card = cardRepository.save(card);
        
        return mapToCardResponse(card, null);
    }
    
    @Transactional
    public CardResponse rejectCard(UUID cardId, String reason) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        
        if (!"Pending".equalsIgnoreCase(card.getStatus())) {
            throw new IllegalArgumentException("Only pending cards can be rejected");
        }
        
        card.setStatus("Rejected");
        card.setCvvHash(reason);
        card = cardRepository.save(card);
        
        return mapToCardResponse(card, null);
    }
    
    private String generateUniqueCardNumber() {
        String cardNumber;
        int attempts = 0;
        do {
            cardNumber = generateRandomNumber(CARD_NUMBER_LENGTH);
            attempts++;
            if (attempts > 10) {
                throw new IllegalStateException("Unable to generate unique card number");
            }
        } while (cardRepository.existsByCardNumber(cardNumber));
        return cardNumber;
    }
    
    private String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }
    
    private String generateCVV() {
        return generateRandomNumber(CVV_LENGTH);
    }
    
    private CardResponse mapToCardResponse(Card card, String cvv) {
        AccountHolder holder = card.getAccount().getHolder();
        return CardResponse.builder()
                .cardId(card.getCardId())
                .accountId(card.getAccount().getAccountId())
                .accountNumber(card.getAccount().getAccountNumber())
                .cardNumber(card.getCardNumber())
                .cvv(cvv)
                .cardType(card.getCardType())
                .expirationDate(card.getExpirationDate())
                .dailyLimit(card.getDailyLimit())
                .status(card.getStatus())
                .issueDate(card.getIssueDate().atStartOfDay())
                .holderName(holder.getFirstName() + " " + holder.getLastName())
                .build();
    }
}
