package com.banking.banking_api.dto;

import com.banking.banking_api.model.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}