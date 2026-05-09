package com.transactionguard.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DTO Tests - Validation and Serialization")
class DtoValidationTest {

    @Nested
    @DisplayName("TransactionRequest Tests")
    class TransactionRequestTests {

        @Test
        @DisplayName("Should create TransactionRequest with all parameters")
        void shouldCreateTransactionRequestWithAllParameters() {
            // Arrange
            Long sourceId = 1L;
            Long targetId = 2L;
            BigDecimal amount = new BigDecimal("100.00");

            // Act
            TransactionRequest request = new TransactionRequest(sourceId, targetId, amount);

            // Assert
            assertThat(request)
                .isNotNull()
                .extracting("sourceAccountId", "targetAccountId", "amount")
                .containsExactly(sourceId, targetId, amount);
        }

        @Test
        @DisplayName("Should create empty TransactionRequest")
        void shouldCreateEmptyTransactionRequest() {
            // Act
            TransactionRequest request = new TransactionRequest();

            // Assert
            assertThat(request).isNotNull();
            assertThat(request.getSourceAccountId()).isNull();
            assertThat(request.getTargetAccountId()).isNull();
            assertThat(request.getAmount()).isNull();
        }

        @Test
        @DisplayName("Should set source account id")
        void shouldSetSourceAccountId() {
            // Arrange
            TransactionRequest request = new TransactionRequest();

            // Act
            request.setSourceAccountId(10L);

            // Assert
            assertThat(request.getSourceAccountId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("Should set target account id")
        void shouldSetTargetAccountId() {
            // Arrange
            TransactionRequest request = new TransactionRequest();

            // Act
            request.setTargetAccountId(20L);

            // Assert
            assertThat(request.getTargetAccountId()).isEqualTo(20L);
        }

        @Test
        @DisplayName("Should set amount")
        void shouldSetAmount() {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            BigDecimal amount = new BigDecimal("250.50");

            // Act
            request.setAmount(amount);

            // Assert
            assertThat(request.getAmount()).isEqualTo(amount);
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            // Arrange
            TransactionRequest request = new TransactionRequest(null, null, null);

            // Act & Assert
            assertThat(request.getSourceAccountId()).isNull();
            assertThat(request.getTargetAccountId()).isNull();
            assertThat(request.getAmount()).isNull();
        }

        @Test
        @DisplayName("Should update all fields")
        void shouldUpdateAllFields() {
            // Arrange
            TransactionRequest request = new TransactionRequest(1L, 2L, new BigDecimal("100.00"));

            // Act
            request.setSourceAccountId(5L);
            request.setTargetAccountId(6L);
            request.setAmount(new BigDecimal("500.00"));

            // Assert
            assertThat(request.getSourceAccountId()).isEqualTo(5L);
            assertThat(request.getTargetAccountId()).isEqualTo(6L);
            assertThat(request.getAmount()).isEqualTo(new BigDecimal("500.00"));
        }
    }

    @Nested
    @DisplayName("TransferRequest Tests")
    class TransferRequestTests {

        @Test
        @DisplayName("Should create TransferRequest with all parameters")
        void shouldCreateTransferRequestWithAllParameters() {
            // Arrange
            Long sourceId = 1L;
            Long targetId = 2L;
            BigDecimal amount = new BigDecimal("100.00");

            // Act
            TransferRequest request = new TransferRequest(sourceId, targetId, amount);

            // Assert
            assertThat(request)
                .isNotNull()
                .extracting("sourceAccountId", "targetAccountId", "amount")
                .containsExactly(sourceId, targetId, amount);
        }

        @Test
        @DisplayName("Should create empty TransferRequest")
        void shouldCreateEmptyTransferRequest() {
            // Act
            TransferRequest request = new TransferRequest();

            // Assert
            assertThat(request).isNotNull();
            assertThat(request.getSourceAccountId()).isNull();
            assertThat(request.getTargetAccountId()).isNull();
            assertThat(request.getAmount()).isNull();
        }

        @Test
        @DisplayName("Should set source account id")
        void shouldSetSourceAccountId() {
            // Arrange
            TransferRequest request = new TransferRequest();

            // Act
            request.setSourceAccountId(10L);

            // Assert
            assertThat(request.getSourceAccountId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("Should set target account id")
        void shouldSetTargetAccountId() {
            // Arrange
            TransferRequest request = new TransferRequest();

            // Act
            request.setTargetAccountId(20L);

            // Assert
            assertThat(request.getTargetAccountId()).isEqualTo(20L);
        }

        @Test
        @DisplayName("Should set amount")
        void shouldSetAmount() {
            // Arrange
            TransferRequest request = new TransferRequest();
            BigDecimal amount = new BigDecimal("750.25");

            // Act
            request.setAmount(amount);

            // Assert
            assertThat(request.getAmount()).isEqualTo(amount);
        }

        @Test
        @DisplayName("Should handle negative amounts")
        void shouldHandleNegativeAmounts() {
            // Arrange
            BigDecimal negativeAmount = new BigDecimal("-100.00");

            // Act
            TransferRequest request = new TransferRequest(1L, 2L, negativeAmount);

            // Assert
            assertThat(request.getAmount()).isNegative();
        }

        @Test
        @DisplayName("Should handle zero amount")
        void shouldHandleZeroAmount() {
            // Arrange
            BigDecimal zeroAmount = BigDecimal.ZERO;

            // Act
            TransferRequest request = new TransferRequest(1L, 2L, zeroAmount);

            // Assert
            assertThat(request.getAmount()).isEqualTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Should handle large amounts")
        void shouldHandleLargeAmounts() {
            // Arrange
            BigDecimal largeAmount = new BigDecimal("999999999.99");

            // Act
            TransferRequest request = new TransferRequest(1L, 2L, largeAmount);

            // Assert
            assertThat(request.getAmount()).isEqualTo(largeAmount);
        }

        @Test
        @DisplayName("Should update all fields")
        void shouldUpdateAllFields() {
            // Arrange
            TransferRequest request = new TransferRequest(1L, 2L, new BigDecimal("100.00"));

            // Act
            request.setSourceAccountId(7L);
            request.setTargetAccountId(8L);
            request.setAmount(new BigDecimal("1000.00"));

            // Assert
            assertThat(request.getSourceAccountId()).isEqualTo(7L);
            assertThat(request.getTargetAccountId()).isEqualTo(8L);
            assertThat(request.getAmount()).isEqualTo(new BigDecimal("1000.00"));
        }
    }

    @Nested
    @DisplayName("DTO Comparison Tests")
    class DtoComparisonTests {

        @Test
        @DisplayName("Should create identical TransactionRequests")
        void shouldCreateIdenticalTransactionRequests() {
            // Arrange
            Long sourceId = 1L;
            Long targetId = 2L;
            BigDecimal amount = new BigDecimal("100.00");

            // Act
            TransactionRequest request1 = new TransactionRequest(sourceId, targetId, amount);
            TransactionRequest request2 = new TransactionRequest(sourceId, targetId, amount);

            // Assert
            assertThat(request1).isNotSameAs(request2);
            assertThat(request1.getSourceAccountId()).isEqualTo(request2.getSourceAccountId());
            assertThat(request1.getTargetAccountId()).isEqualTo(request2.getTargetAccountId());
            assertThat(request1.getAmount()).isEqualTo(request2.getAmount());
        }

        @Test
        @DisplayName("Should create identical TransferRequests")
        void shouldCreateIdenticalTransferRequests() {
            // Arrange
            Long sourceId = 3L;
            Long targetId = 4L;
            BigDecimal amount = new BigDecimal("250.75");

            // Act
            TransferRequest request1 = new TransferRequest(sourceId, targetId, amount);
            TransferRequest request2 = new TransferRequest(sourceId, targetId, amount);

            // Assert
            assertThat(request1).isNotSameAs(request2);
            assertThat(request1.getSourceAccountId()).isEqualTo(request2.getSourceAccountId());
            assertThat(request1.getTargetAccountId()).isEqualTo(request2.getTargetAccountId());
            assertThat(request1.getAmount()).isEqualTo(request2.getAmount());
        }
    }
}
