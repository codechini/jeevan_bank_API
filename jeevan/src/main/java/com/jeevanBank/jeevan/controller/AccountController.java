package com.jeevanBank.jeevan.controller;

import com.jeevanBank.jeevan.dto.AccountDto;
import com.jeevanBank.jeevan.dto.AmountRequestDto;
import com.jeevanBank.jeevan.service.AccountService;
//import jakarta.validation.Valid;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8082"})
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    // create account for existing user
    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createAccount(@PathVariable Long userId) throws Exception {
        var account = accountService.createAccountForUser(userId);
        return ResponseEntity.ok(accountService.getAccountDtoByUserId(userId));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long accountId){
        Double balance = accountService.getBalanceByAccountId(accountId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long accountId,
                                     @Valid @RequestBody AmountRequestDto req) {
        AccountDto dto = accountService.deposit(accountId, req.getAmount());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId,
                                      @Valid @RequestBody AmountRequestDto req) throws Exception {
        AccountDto dto = accountService.withdraw(accountId, req.getAmount());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> transactions(@PathVariable Long accountId){
        return ResponseEntity.ok(accountService.getTransactions(accountId));
    }
}
