package com.banking.banking_api.dto;

import com.banking.banking_api.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private Long toAccountId;

    private String description;
}