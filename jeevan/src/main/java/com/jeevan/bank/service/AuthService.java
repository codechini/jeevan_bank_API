package com.jeevan.bank.service;

import com.jeevan.bank.dto.*;
import com.jeevan.bank.entity.Account;
import com.jeevan.bank.entity.AccountHolder;
import com.jeevan.bank.entity.Role;
import com.jeevan.bank.entity.User;
import com.jeevan.bank.repository.AccountHolderRepository;
import com.jeevan.bank.repository.AccountRepository;
import com.jeevan.bank.repository.RoleRepository;
import com.jeevan.bank.repository.UserRepository;
import com.jeevan.bank.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return new JwtResponse(jwt, user.getUsername(), user.getRole().getRoleName());
    }
    
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));
        
        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(userRole)
                .build();
        
        user = userRepository.save(user);
        
        AccountHolder holder = AccountHolder.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();
        
        accountHolderRepository.save(holder);
        
        String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());
        
        return new JwtResponse(jwt, user.getUsername(), user.getRole().getRoleName());
    }
    
    @Transactional
    public AccountResponse openAccount(UUID userId, OpenAccountRequest request) {
        AccountHolder holder = accountHolderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        
        if (request.getFirstName() != null) {
            holder.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            holder.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null) {
            holder.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            holder.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            holder.setPhone(request.getPhone());
        }
        if (request.getCitizenshipId() != null) {
            holder.setCitizenshipId(request.getCitizenshipId());
        }
        
        accountHolderRepository.save(holder);
        
        String accountNumber = generateAccountNumber();
        
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .holder(holder)
                .accountType(request.getAccountType())
                .build();
        
        account = accountRepository.save(account);
        
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .holderName(holder.getFirstName() + " " + holder.getLastName())
                .build();
    }
    
    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("JB");
        for (int i = 0; i < 18; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    @Transactional
    public AccountResponse openAccountByAdmin(UUID adminId, AdminOpenAccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AccountHolder holder = accountHolderRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Account holder not found for this user"));
        
        if (!holder.getIsActive()) {
            throw new IllegalArgumentException("Cannot open account for inactive user");
        }
        
        if (request.getFirstName() != null) {
            holder.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            holder.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null) {
            holder.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            holder.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            holder.setPhone(request.getPhone());
        }
        if (request.getCitizenshipId() != null) {
            holder.setCitizenshipId(request.getCitizenshipId());
        }
        
        accountHolderRepository.save(holder);
        
        String accountNumber = generateAccountNumber();
        
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .holder(holder)
                .accountType(request.getAccountType())
                .build();
        
        account = accountRepository.save(account);
        
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .holderName(holder.getFirstName() + " " + holder.getLastName())
                .build();
    }
}
