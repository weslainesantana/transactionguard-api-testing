package com.transactionguard.repository;

import com.transactionguard.domain.Transaction;
import com.transactionguard.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TransactionRepository Integration Tests")
class TransactionRepositoryIntegrationTest {

    private TransactionRepository repository = new InMemoryTransactionRepository();

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
        Transaction saved = repository.save(transaction);

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
        Transaction saved = repository.save(transaction);

        // Act
        Optional<Transaction> found = repository.findById(saved.getId());

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
        repository.save(new Transaction(sourceAccountId, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS));
        repository.save(new Transaction(sourceAccountId, 3L, new BigDecimal("50.00"), TransactionStatus.SUCCESS));
        repository.save(new Transaction(4L, 2L, new BigDecimal("75.00"), TransactionStatus.SUCCESS));

        // Act
        List<Transaction> found = repository.findBySourceAccountId(sourceAccountId);

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
        repository.save(new Transaction(1L, targetAccountId, new BigDecimal("100.00"), TransactionStatus.SUCCESS));
        repository.save(new Transaction(3L, targetAccountId, new BigDecimal("50.00"), TransactionStatus.SUCCESS));
        repository.save(new Transaction(1L, 4L, new BigDecimal("75.00"), TransactionStatus.SUCCESS));

        // Act
        List<Transaction> found = repository.findByTargetAccountId(targetAccountId);

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
        Transaction saved = repository.save(transaction);

        // Act
        saved.setStatus(TransactionStatus.SUCCESS);
        repository.save(saved);

        // Assert
        Transaction updated = repository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    @DisplayName("Should delete transaction")
    void shouldDeleteTransaction() {
        // Arrange
        Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS);
        Transaction saved = repository.save(transaction);

        // Act
        repository.deleteById(saved.getId());

        // Assert
        Optional<Transaction> deleted = repository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when no transactions found")
    void shouldReturnEmptyListWhenNoTransactionsFound() {
        // Act
        List<Transaction> found = repository.findBySourceAccountId(999L);

        // Assert
        assertThat(found).isEmpty();
    }

    // In-memory implementation for testing
    static class InMemoryTransactionRepository implements TransactionRepository {
        private final Map<Long, Transaction> store = new HashMap<>();
        private Long nextId = 1L;

        @Override
        public Transaction save(Transaction transaction) {
            if (transaction.getId() == null) {
                transaction.setId(nextId++);
            }
            store.put(transaction.getId(), transaction);
            return transaction;
        }

        @Override
        public Optional<Transaction> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Transaction> findBySourceAccountId(Long sourceAccountId) {
            return store.values().stream()
                .filter(t -> t.getSourceAccountId().equals(sourceAccountId))
                .toList();
        }

        @Override
        public List<Transaction> findByTargetAccountId(Long targetAccountId) {
            return store.values().stream()
                .filter(t -> t.getTargetAccountId().equals(targetAccountId))
                .toList();
        }

        @Override
        public void deleteById(Long id) {
            store.remove(id);
        }
    }
}
