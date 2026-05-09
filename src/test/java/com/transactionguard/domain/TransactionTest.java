package com.transactionguard.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Transaction Domain Tests")
class TransactionTest {

    @Nested
    @DisplayName("Transaction Creation Tests")
    class TransactionCreationTests {

        @Test
        @DisplayName("Should create transaction with all parameters")
        void shouldCreateTransactionWithAllParameters() {
            // Arrange
            Long sourceId = 1L;
            Long targetId = 2L;
            BigDecimal amount = new BigDecimal("100.00");
            TransactionStatus status = TransactionStatus.SUCCESS;

            // Act
            Transaction transaction = new Transaction(sourceId, targetId, amount, status);

            // Assert
            assertThat(transaction)
                .isNotNull()
                .extracting(
                    "sourceAccountId",
                    "targetAccountId",
                    "amount",
                    "status"
                )
                .containsExactly(sourceId, targetId, amount, status);

            assertThat(transaction.getCreatedAt())
                .isNotNull()
                .isBefore(LocalDateTime.now().plusSeconds(1));
        }

        @Test
        @DisplayName("Should create empty transaction")
        void shouldCreateEmptyTransaction() {
            // Act
            Transaction transaction = new Transaction();

            // Assert
            assertThat(transaction).isNotNull();
        }

        @Test
        @DisplayName("Should set transaction id")
        void shouldSetTransactionId() {
            // Arrange
            Transaction transaction = new Transaction();
            Long id = 123L;

            // Act
            transaction.setId(id);

            // Assert
            assertThat(transaction.getId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("Transaction Status Tests")
    class TransactionStatusTests {

        @Test
        @DisplayName("Should have all status values")
        void shouldHaveAllStatusValues() {
            // Act & Assert
            assertThat(TransactionStatus.values())
                .contains(
                    TransactionStatus.PENDING,
                    TransactionStatus.SUCCESS,
                    TransactionStatus.FAILED,
                    TransactionStatus.CANCELLED
                )
                .hasSize(4);
        }

        @Test
        @DisplayName("Should set transaction status to SUCCESS")
        void shouldSetTransactionStatusToSuccess() {
            // Arrange
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.PENDING);

            // Act
            transaction.setStatus(TransactionStatus.SUCCESS);

            // Assert
            assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
        }

        @Test
        @DisplayName("Should set transaction status to FAILED")
        void shouldSetTransactionStatusToFailed() {
            // Arrange
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.PENDING);

            // Act
            transaction.setStatus(TransactionStatus.FAILED);

            // Assert
            assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.FAILED);
        }

        @Test
        @DisplayName("Should set transaction status to CANCELLED")
        void shouldSetTransactionStatusToCancelled() {
            // Arrange
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.PENDING);

            // Act
            transaction.setStatus(TransactionStatus.CANCELLED);

            // Assert
            assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("Transaction Amount Tests")
    class TransactionAmountTests {

        @Test
        @DisplayName("Should handle large amounts")
        void shouldHandleLargeAmounts() {
            // Arrange
            BigDecimal largeAmount = new BigDecimal("999999.99");

            // Act
            Transaction transaction = new Transaction(1L, 2L, largeAmount, TransactionStatus.SUCCESS);

            // Assert
            assertThat(transaction.getAmount()).isEqualTo(largeAmount);
        }

        @Test
        @DisplayName("Should handle small amounts")
        void shouldHandleSmallAmounts() {
            // Arrange
            BigDecimal smallAmount = new BigDecimal("0.01");

            // Act
            Transaction transaction = new Transaction(1L, 2L, smallAmount, TransactionStatus.SUCCESS);

            // Assert
            assertThat(transaction.getAmount()).isEqualTo(smallAmount);
        }

        @Test
        @DisplayName("Should set amount after creation")
        void shouldSetAmountAfterCreation() {
            // Arrange
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS);
            BigDecimal newAmount = new BigDecimal("200.00");

            // Act
            transaction.setAmount(newAmount);

            // Assert
            assertThat(transaction.getAmount()).isEqualTo(newAmount);
        }
    }

    @Nested
    @DisplayName("Transaction Accounts Tests")
    class TransactionAccountsTests {

        @Test
        @DisplayName("Should set source account id")
        void shouldSetSourceAccountId() {
            // Arrange
            Transaction transaction = new Transaction();
            Long sourceId = 100L;

            // Act
            transaction.setSourceAccountId(sourceId);

            // Assert
            assertThat(transaction.getSourceAccountId()).isEqualTo(sourceId);
        }

        @Test
        @DisplayName("Should set target account id")
        void shouldSetTargetAccountId() {
            // Arrange
            Transaction transaction = new Transaction();
            Long targetId = 200L;

            // Act
            transaction.setTargetAccountId(targetId);

            // Assert
            assertThat(transaction.getTargetAccountId()).isEqualTo(targetId);
        }

        @Test
        @DisplayName("Should handle different source and target accounts")
        void shouldHandleDifferentSourceAndTargetAccounts() {
            // Arrange & Act
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS);

            // Assert
            assertThat(transaction.getSourceAccountId())
                .isNotEqualTo(transaction.getTargetAccountId());
        }
    }

    @Nested
    @DisplayName("Transaction Timestamp Tests")
    class TransactionTimestampTests {

        @Test
        @DisplayName("Should set created timestamp automatically")
        void shouldSetCreatedTimestampAutomatically() {
            // Arrange
            LocalDateTime beforeCreation = LocalDateTime.now();

            // Act
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.SUCCESS);
            LocalDateTime afterCreation = LocalDateTime.now();

            // Assert
            assertThat(transaction.getCreatedAt())
                .isNotNull()
                .isAfterOrEqualTo(beforeCreation)
                .isBeforeOrEqualTo(afterCreation);
        }

        @Test
        @DisplayName("Should set custom created timestamp")
        void shouldSetCustomCreatedTimestamp() {
            // Arrange
            Transaction transaction = new Transaction();
            LocalDateTime customTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

            // Act
            transaction.setCreatedAt(customTime);

            // Assert
            assertThat(transaction.getCreatedAt()).isEqualTo(customTime);
        }

        @Test
        @DisplayName("Should preserve timestamp when updating other fields")
        void shouldPreserveTimestampWhenUpdatingOtherFields() {
            // Arrange
            Transaction transaction = new Transaction(1L, 2L, new BigDecimal("100.00"), TransactionStatus.PENDING);
            LocalDateTime originalTime = transaction.getCreatedAt();

            // Act
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setAmount(new BigDecimal("150.00"));

            // Assert
            assertThat(transaction.getCreatedAt()).isEqualTo(originalTime);
        }
    }

    @Nested
    @DisplayName("Transaction Getters/Setters Tests")
    class TransactionGettersSettersTests {

        @Test
        @DisplayName("Should get all transaction properties")
        void shouldGetAllTransactionProperties() {
            // Arrange
            Long id = 1L;
            Long sourceId = 10L;
            Long targetId = 20L;
            BigDecimal amount = new BigDecimal("500.00");
            TransactionStatus status = TransactionStatus.SUCCESS;
            LocalDateTime createdAt = LocalDateTime.now();

            Transaction transaction = new Transaction(sourceId, targetId, amount, status);
            transaction.setId(id);
            transaction.setCreatedAt(createdAt);

            // Act & Assert
            assertThat(transaction.getId()).isEqualTo(id);
            assertThat(transaction.getSourceAccountId()).isEqualTo(sourceId);
            assertThat(transaction.getTargetAccountId()).isEqualTo(targetId);
            assertThat(transaction.getAmount()).isEqualTo(amount);
            assertThat(transaction.getStatus()).isEqualTo(status);
            assertThat(transaction.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("Should update all transaction properties")
        void shouldUpdateAllTransactionProperties() {
            // Arrange
            Transaction transaction = new Transaction();

            // Act
            transaction.setId(99L);
            transaction.setSourceAccountId(1L);
            transaction.setTargetAccountId(2L);
            transaction.setAmount(new BigDecimal("999.99"));
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setCreatedAt(LocalDateTime.of(2024, 6, 1, 10, 30, 0));

            // Assert
            assertThat(transaction.getId()).isEqualTo(99L);
            assertThat(transaction.getSourceAccountId()).isEqualTo(1L);
            assertThat(transaction.getTargetAccountId()).isEqualTo(2L);
            assertThat(transaction.getAmount()).isEqualTo(new BigDecimal("999.99"));
            assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.FAILED);
            assertThat(transaction.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 6, 1, 10, 30, 0));
        }
    }
}
