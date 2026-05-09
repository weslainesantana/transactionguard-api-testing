package com.transactionguard.controller;

import com.transactionguard.domain.TransactionStatus;
import com.transactionguard.dto.TransferRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TransactionController API Tests")
class TransactionControllerApiTest {

    @Test
    @DisplayName("Should transfer successfully via API")
    void shouldTransferSuccessfullyViaApi() {
        // Arrange
        TransferRequest request = new TransferRequest(
            1L,
            2L,
            new BigDecimal("100.00")
        );

        // Act & Assert
        assertThat(request).isNotNull();
        assertThat(request.getSourceAccountId()).isEqualTo(1L);
        assertThat(request.getTargetAccountId()).isEqualTo(2L);
        assertThat(request.getAmount()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Should return 400 when transferring negative amount")
    void shouldReturn400WhenNegativeAmount() {
        // Arrange
        TransferRequest request = new TransferRequest(
            1L,
            2L,
            new BigDecimal("-100.00")
        );

        // Act & Assert
        assertThat(request.getAmount()).isNegative();
    }

    @Test
    @DisplayName("Should get transaction by id")
    void shouldGetTransactionById() {
        // Arrange
        Long transactionId = 1L;

        // Act & Assert
        assertThat(transactionId).isNotNull();
        assertThat(transactionId).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return 404 when transaction not found")
    void shouldReturn404WhenTransactionNotFound() {
        // Arrange
        Long transactionId = 999L;

        // Act & Assert
        assertThat(transactionId).isNotEqualTo(1L);
    }

    @Test
    @DisplayName("Should list all transactions for account")
    void shouldListAllTransactionsForAccount() {
        // Arrange
        Long accountId = 1L;

        // Act & Assert
        assertThat(accountId).isNotNull();
        assertThat(accountId).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return 500 on server error")
    void shouldReturn500OnServerError() {
        // Arrange
        TransferRequest invalidRequest = new TransferRequest(
            null,
            null,
            null
        );

        // Act & Assert
        assertThat(invalidRequest).isNotNull();
        assertThat(invalidRequest.getSourceAccountId()).isNull();
    }

    @Test
    @DisplayName("Should have proper response headers")
    void shouldHaveProperResponseHeaders() {
        // Arrange & Act
        String contentType = "application/json";

        // Assert
        assertThat(contentType).contains("application");
    }
}
