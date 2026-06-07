package com.banking.banking_api.service;

import com.banking.banking_api.dto.TransactionRequest;
import com.banking.banking_api.dto.TransactionResponse;
import com.banking.banking_api.model.Account;
import com.banking.banking_api.model.Transaction;
import com.banking.banking_api.model.TransactionType;
import com.banking.banking_api.repository.AccountRepository;
import com.banking.banking_api.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final BigDecimal FRAUD_THRESHOLD = new BigDecimal("10000");

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponse processTransaction(String username, TransactionRequest request) {
        Account fromAccount = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!fromAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        Transaction transaction = new Transaction();
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setFromAccount(fromAccount);

        boolean flagged = request.getAmount().compareTo(FRAUD_THRESHOLD) > 0;
        transaction.setFlagged(flagged);

        if (request.getType() == TransactionType.DEPOSIT) {
            fromAccount.setBalance(fromAccount.getBalance().add(request.getAmount()));
        } else if (request.getType() == TransactionType.WITHDRAWAL) {
            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds");
            }
            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        } else if (request.getType() == TransactionType.TRANSFER) {
            if (request.getToAccountId() == null) {
                throw new RuntimeException("Target account is required for transfers");
            }
            Account toAccount = accountRepository.findById(request.getToAccountId())
                    .orElseThrow(() -> new RuntimeException("Target account not found"));

            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
            transaction.setToAccount(toAccount);
            accountRepository.save(toAccount);
        }

        accountRepository.save(fromAccount);
        transactionRepository.save(transaction);

        return mapToResponse(transaction, flagged);
    }

    public List<TransactionResponse> getTransactionHistory(Long accountId, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        return transactionRepository
                .findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(accountId, accountId)
                .stream()
                .map(t -> mapToResponse(t, t.isFlagged()))
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction, boolean flagged) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getFromAccount() != null ? transaction.getFromAccount().getId() : null,
                transaction.getToAccount() != null ? transaction.getToAccount().getId() : null,
                transaction.getDescription(),
                flagged,
                transaction.getCreatedAt()
        );
    }
}