package com.jeevan.bank.config;

import com.jeevan.bank.entity.Role;
import com.jeevan.bank.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    
    @Override
    public void run(String... args) {
        initRoles();
    }
    
    private void initRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = Role.builder().roleName("USER").build();
            Role adminRole = Role.builder().roleName("ADMIN").build();
            
            roleRepository.save(userRole);
            roleRepository.save(adminRole);
            
            log.info("Initialized default roles: USER, ADMIN");
        }
    }
}
