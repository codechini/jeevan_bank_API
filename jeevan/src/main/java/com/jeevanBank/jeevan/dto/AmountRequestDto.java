package com.jeevanBank.jeevan.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmountRequestDto {
    @NotNull
    @Min(value = 1, message = "Amount must be at least 1")
    private Double amount;
}
