package com.banking.banking_api.controller;

import com.banking.banking_api.dto.AccountResponse;
import com.banking.banking_api.dto.CreateAccountRequest;
import com.banking.banking_api.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.createAccount(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getUserAccounts(userDetails.getUsername()));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getAccount(accountId, userDetails.getUsername()));
    }
}