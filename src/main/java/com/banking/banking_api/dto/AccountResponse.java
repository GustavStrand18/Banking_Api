package com.banking.banking_api.dto;

import com.banking.banking_api.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}