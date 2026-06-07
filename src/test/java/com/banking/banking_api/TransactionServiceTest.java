package com.banking.banking_api;

import com.banking.banking_api.dto.TransactionRequest;
import com.banking.banking_api.dto.TransactionResponse;
import com.banking.banking_api.model.*;
import com.banking.banking_api.repository.AccountRepository;
import com.banking.banking_api.repository.TransactionRepository;
import com.banking.banking_api.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC123");
        testAccount.setAccountType(AccountType.CHECKING);
        testAccount.setBalance(new BigDecimal("1000"));
        testAccount.setUser(testUser);
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.DEPOSIT);
        request.setAmount(new BigDecimal("500"));
        request.setAccountId(1L);
        request.setDescription("Test deposit");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionResponse response = transactionService.processTransaction("testuser", request);

        assertEquals(new BigDecimal("1500"), testAccount.getBalance());
        assertEquals(TransactionType.DEPOSIT, response.getType());
        assertFalse(response.isFlagged());
    }

    @Test
    void withdrawal_shouldDecreaseBalance() {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.WITHDRAWAL);
        request.setAmount(new BigDecimal("500"));
        request.setAccountId(1L);
        request.setDescription("Test withdrawal");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionResponse response = transactionService.processTransaction("testuser", request);

        assertEquals(new BigDecimal("500"), testAccount.getBalance());
        assertEquals(TransactionType.WITHDRAWAL, response.getType());
    }

    @Test
    void withdrawal_insufficientFunds_shouldThrowException() {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.WITHDRAWAL);
        request.setAmount(new BigDecimal("9999"));
        request.setAccountId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.processTransaction("testuser", request));

        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    void largeTransaction_shouldBeFlagged() {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.DEPOSIT);
        request.setAmount(new BigDecimal("50000"));
        request.setAccountId(1L);
        request.setDescription("Large deposit");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionResponse response = transactionService.processTransaction("testuser", request);

        assertTrue(response.isFlagged());
    }

    @Test
    void unauthorizedAccess_shouldThrowException() {
        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.DEPOSIT);
        request.setAmount(new BigDecimal("500"));
        request.setAccountId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.processTransaction("wronguser", request));

        assertEquals("Unauthorized access to account", exception.getMessage());
    }
}