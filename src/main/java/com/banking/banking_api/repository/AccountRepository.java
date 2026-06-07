package com.banking.banking_api.repository;

import com.banking.banking_api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Optional<Account> findByAccountNumber(String accountNumber);
}