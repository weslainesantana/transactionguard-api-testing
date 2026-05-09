package com.transactionguard.repository;

import com.transactionguard.domain.Transaction;
import com.transactionguard.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
@DisplayName("TransactionRepository Integration Tests")
class TransactionRepositoryIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("Should save transaction successfully")
    void shouldSaveTransaction() {
        // Arrange
        Transaction transaction = new Transaction(
            1L, 2L,
            new BigDecimal("100.00"),
            TransactionStatus.SUCCESS
        );

        // Act
        Transaction saved = transactionRepository.save(transaction);

        // Assert
        assertThat(saved)
            .isNotNull()
            .extracting("id")
            .isNotNull();
    }

    @Test
    @DisplayName("Should find transaction by id")
    void shouldFindTransactionById() {
        // Arrange
        Transaction transaction = new Transaction(
            1L, 2L,
            new BigDecimal("100.00"),
            TransactionStatus.SUCCESS
        );
        Transaction saved = transactionRepository.save(transaction);

        // Act
        Optional<Transaction> found = transactionRepository.findById(saved.getId());

        // Assert
        assertThat(found)
            .isPresent()
            .get()
            .extracting("sourceAccountId")
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("Should find all transactions by source account")
    void shouldFindTransactionsBySourceAccount() {
        // Arrange
        Long sourceAccountId = 1L;
        transactionRepository.save(new Transaction(sourceAccountId, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS));
        transactionRepository.save(new Transaction(sourceAccountId, 3L, new BigDecimal("50.00"), TransactionStatus.SUCCESS));
        transactionRepository.save(new Transaction(4L, 2L, new BigDecimal("75.00"), TransactionStatus.SUCCESS));

        // Act
        List<Transaction> found = transactionRepository.findBySourceAccountId(sourceAccountId);

        // Assert
        assertThat(found)
            .hasSize(2)
            .allMatch(t -> t.getSourceAccountId().equals(sourceAccountId));
    }

    @Test
    @DisplayName("Should find all transactions by target account")
    void shouldFindTransactionsByTargetAccount() {
        // Arrange
        Long targetAccountId = 2L;
        transactionRepository.save(new Transaction(1L, targetAccountId, new BigDecimal("100.00"), TransactionStatus.SUCCESS));
        transactionRepository.save(new Transaction(3L, targetAccountId, new BigDecimal("50.00"), TransactionStatus.SUCCESS));
        transactionRepository.save(new Transaction(1L, 4L, new BigDecimal("75.00"), TransactionStatus.SUCCESS));

        // Act
        List<Transaction> found = transactionRepository.findByTargetAccountId(targetAccountId);

        // Assert
        assertThat(found)
            .hasSize(2)
            .allMatch(t -> t.getTargetAccountId().equals(targetAccountId));
    }

    @Test
    @DisplayName("Should update transaction status")
    void shouldUpdateTransactionStatus() {
        // Arrange
        Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.PENDING);
        Transaction saved = transactionRepository.save(transaction);

        // Act
        saved.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(saved);

        // Assert
        Transaction updated = transactionRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    @DisplayName("Should delete transaction")
    void shouldDeleteTransaction() {
        // Arrange
        Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS);
        Transaction saved = transactionRepository.save(transaction);

        // Act
        transactionRepository.deleteById(saved.getId());

        // Assert
        Optional<Transaction> deleted = transactionRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when no transactions found")
    void shouldReturnEmptyListWhenNoTransactionsFound() {
        // Act
        List<Transaction> found = transactionRepository.findBySourceAccountId(999L);

        // Assert
        assertThat(found).isEmpty();
    }
}
