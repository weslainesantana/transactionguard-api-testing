package com.transactionguard.service;

import com.transactionguard.domain.Transaction;
import com.transactionguard.domain.TransactionStatus;
import com.transactionguard.dto.TransactionRequest;
import com.transactionguard.exception.InsufficientFundsException;
import com.transactionguard.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService Unit Tests")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new TransactionRequest(
            1L,
            2L,
            new BigDecimal("100.00")
        );
    }

    @Test
    @DisplayName("Should transfer successfully with valid account")
    void shouldTransferSuccessfully() {
        // Arrange
        Transaction expectedTransaction = new Transaction(
            1L, 2L,
            new BigDecimal("100.00"),
            TransactionStatus.SUCCESS
        );
        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(expectedTransaction);

        // Act
        Transaction result = transactionService.transfer(validRequest);

        // Assert
        assertThat(result)
            .isNotNull()
            .extracting("status")
            .isEqualTo(TransactionStatus.SUCCESS);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should fail transfer with insufficient funds")
    void shouldFailTransferWithInsufficientFunds() {
        // Arrange
        TransactionRequest largeRequest = new TransactionRequest(
            1L, 2L,
            new BigDecimal("9999.99")
        );

        // Act & Assert
        assertThatThrownBy(() -> transactionService.transfer(largeRequest))
            .isInstanceOf(InsufficientFundsException.class)
            .hasMessage("Insufficient funds for transfer");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail transfer with invalid amount")
    void shouldFailTransferWithInvalidAmount() {
        // Arrange
        TransactionRequest invalidRequest = new TransactionRequest(
            1L, 2L,
            new BigDecimal("-100.00")
        );

        // Act & Assert
        assertThatThrownBy(() -> transactionService.transfer(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Amount must be greater than zero");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail transfer with same source and target")
    void shouldFailTransferToSameAccount() {
        // Arrange
        TransactionRequest sameAccountRequest = new TransactionRequest(
            1L, 1L,
            new BigDecimal("100.00")
        );

        // Act & Assert
        assertThatThrownBy(() -> transactionService.transfer(sameAccountRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Source and target accounts cannot be the same");

        verify(transactionRepository, never()).save(any());
    }
}
