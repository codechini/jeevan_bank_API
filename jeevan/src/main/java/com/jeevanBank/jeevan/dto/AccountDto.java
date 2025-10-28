package com.jeevanBank.jeevan.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long accountId;
    private String accountNumber;
    private Double balance;
    private Long userId;
}
