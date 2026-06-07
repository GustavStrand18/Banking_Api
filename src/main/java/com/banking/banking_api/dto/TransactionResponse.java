package com.banking.banking_api.dto;

import com.banking.banking_api.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private Long fromAccountId;
    private Long toAccountId;
    private String description;
    private boolean flagged;
    private LocalDateTime createdAt;
}