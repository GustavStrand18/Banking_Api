package com.banking.banking_api.controller;

import com.banking.banking_api.dto.TransactionRequest;
import com.banking.banking_api.dto.TransactionResponse;
import com.banking.banking_api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> processTransaction(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transactionService.processTransaction(userDetails.getUsername(), request));
    }

    @GetMapping("/{accountId}/history")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId, userDetails.getUsername()));
    }
}