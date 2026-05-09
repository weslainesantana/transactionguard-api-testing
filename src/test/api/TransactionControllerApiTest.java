package com.transactionguard.controller;

import com.transactionguard.domain.TransactionStatus;
import com.transactionguard.dto.TransferRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("TransactionController API Tests")
class TransactionControllerApiTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        RestAssured.baseURI = baseUrl;
        RestAssured.port = port;
    }

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
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/transactions/transfer")
        .then()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("status", equalTo(TransactionStatus.SUCCESS.name()));
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
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/transactions/transfer")
        .then()
            .statusCode(400)
            .body("error", notNullValue());
    }

    @Test
    @DisplayName("Should get transaction by id")
    void shouldGetTransactionById() {
        // Arrange
        Long transactionId = 1L;

        // Act & Assert
        given()
        .when()
            .get("/api/transactions/{id}", transactionId)
        .then()
            .statusCode(200)
            .body("id", equalTo(transactionId.intValue()))
            .body("sourceAccountId", notNullValue())
            .body("targetAccountId", notNullValue())
            .body("amount", notNullValue());
    }

    @Test
    @DisplayName("Should return 404 when transaction not found")
    void shouldReturn404WhenTransactionNotFound() {
        // Act & Assert
        given()
        .when()
            .get("/api/transactions/{id}", 999L)
        .then()
            .statusCode(404)
            .body("error", notNullValue());
    }

    @Test
    @DisplayName("Should list all transactions for account")
    void shouldListAllTransactionsForAccount() {
        // Arrange
        Long accountId = 1L;

        // Act & Assert
        given()
        .when()
            .get("/api/transactions/account/{accountId}", accountId)
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
            .body("[0].sourceAccountId", notNullValue());
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
        given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
        .when()
            .post("/api/transactions/transfer")
        .then()
            .statusCode(500);
    }

    @Test
    @DisplayName("Should have proper response headers")
    void shouldHaveProperResponseHeaders() {
        // Act & Assert
        given()
        .when()
            .get("/api/transactions/1")
        .then()
            .header("Content-Type", containsString("application/json"))
            .header("X-Correlation-ID", notNullValue());
    }
}
