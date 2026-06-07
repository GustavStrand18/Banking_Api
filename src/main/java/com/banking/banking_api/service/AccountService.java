package com.banking.banking_api.service;

import com.banking.banking_api.dto.AccountResponse;
import com.banking.banking_api.dto.CreateAccountRequest;
import com.banking.banking_api.model.Account;
import com.banking.banking_api.model.User;
import com.banking.banking_api.repository.AccountRepository;
import com.banking.banking_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountResponse createAccount(String username, CreateAccountRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setAccountType(request.getAccountType());
        account.setAccountNumber(generateAccountNumber());

        accountRepository.save(account);

        return mapToResponse(account);
    }

    public List<AccountResponse> getUserAccounts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return accountRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AccountResponse getAccount(Long accountId, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to account");
        }

        return mapToResponse(account);
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
}