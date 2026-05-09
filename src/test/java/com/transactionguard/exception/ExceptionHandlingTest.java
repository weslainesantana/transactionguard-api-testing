package com.transactionguard.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Exception Tests - Financial Backend Error Handling")
class ExceptionHandlingTest {

    @Nested
    @DisplayName("InsufficientFundsException Tests")
    class InsufficientFundsExceptionTests {

        @Test
        @DisplayName("Should throw InsufficientFundsException with message")
        void shouldThrowInsufficientFundsException() {
            // Act & Assert
            assertThatThrownBy(() -> {
                throw new InsufficientFundsException("Account balance is R$ 100.00, but trying to transfer R$ 500.00");
            })
                .isInstanceOf(InsufficientFundsException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account balance is R$ 100.00, but trying to transfer R$ 500.00")
                .hasNoCause();
        }

        @Test
        @DisplayName("Should throw InsufficientFundsException with cause")
        void shouldThrowInsufficientFundsExceptionWithCause() {
            // Arrange
            Exception cause = new IllegalStateException("Balance query failed");

            // Act & Assert
            assertThatThrownBy(() -> {
                throw new InsufficientFundsException("Unable to verify balance", cause);
            })
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Unable to verify balance")
                .hasCause(cause);
        }

        @Test
        @DisplayName("Should catch InsufficientFundsException")
        void shouldCatchInsufficientFundsException() {
            // Act & Assert
            try {
                throw new InsufficientFundsException("Saldo insuficiente");
            } catch (InsufficientFundsException e) {
                assertThat(e.getMessage()).contains("Saldo insuficiente");
                assertThat(e).isInstanceOf(RuntimeException.class);
            }
        }
    }

    @Nested
    @DisplayName("InvalidJwtTokenException Tests")
    class InvalidJwtTokenExceptionTests {

        @Test
        @DisplayName("Should throw InvalidJwtTokenException with message")
        void shouldThrowInvalidJwtTokenException() {
            // Act & Assert
            assertThatThrownBy(() -> {
                throw new InvalidJwtTokenException("JWT token has expired");
            })
                .isInstanceOf(InvalidJwtTokenException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("JWT token has expired")
                .hasNoCause();
        }

        @Test
        @DisplayName("Should throw InvalidJwtTokenException with cause")
        void shouldThrowInvalidJwtTokenExceptionWithCause() {
            // Arrange
            Exception cause = new IllegalArgumentException("Invalid signature");

            // Act & Assert
            assertThatThrownBy(() -> {
                throw new InvalidJwtTokenException("Token validation failed", cause);
            })
                .isInstanceOf(InvalidJwtTokenException.class)
                .hasMessage("Token validation failed")
                .hasCause(cause);
        }

        @Test
        @DisplayName("Should handle expired token scenario")
        void shouldHandleExpiredTokenScenario() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDk0NjI0MDB9.invalid");
            })
                .isInstanceOf(InvalidJwtTokenException.class)
                .hasMessage("JWT token has expired");
        }

        @Test
        @DisplayName("Should handle malformed token scenario")
        void shouldHandleMalformedTokenScenario() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateToken("invalid_token_format");
            })
                .isInstanceOf(InvalidJwtTokenException.class)
                .hasMessage("JWT token format is invalid");
        }

        private void validateToken(String token) {
            if (!token.contains(".")) {
                throw new InvalidJwtTokenException("JWT token format is invalid");
            }
            throw new InvalidJwtTokenException("JWT token has expired");
        }
    }

    @Nested
    @DisplayName("UserNotFoundException Tests")
    class UserNotFoundExceptionTests {

        @Test
        @DisplayName("Should throw UserNotFoundException with message")
        void shouldThrowUserNotFoundException() {
            // Act & Assert
            assertThatThrownBy(() -> {
                throw new UserNotFoundException("User with ID 999 not found");
            })
                .isInstanceOf(UserNotFoundException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User with ID 999 not found")
                .hasNoCause();
        }

        @Test
        @DisplayName("Should throw UserNotFoundException with cause")
        void shouldThrowUserNotFoundExceptionWithCause() {
            // Arrange
            Exception cause = new IllegalStateException("Database connection failed");

            // Act & Assert
            assertThatThrownBy(() -> {
                throw new UserNotFoundException("Failed to fetch user data", cause);
            })
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Failed to fetch user data")
                .hasCause(cause);
        }

        @Test
        @DisplayName("Should handle user not found in authentication")
        void shouldHandleUserNotFoundInAuthentication() {
            // Act & Assert
            assertThatThrownBy(() -> {
                findUserByEmail("nonexistent@example.com");
            })
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with email: nonexistent@example.com");
        }

        @Test
        @DisplayName("Should handle user deactivated scenario")
        void shouldHandleUserDeactivatedScenario() {
            // Act & Assert
            assertThatThrownBy(() -> {
                findActiveUser(123L);
            })
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User is deactivated or not found");
        }

        private void findUserByEmail(String email) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        private void findActiveUser(Long userId) {
            throw new UserNotFoundException("User is deactivated or not found");
        }
    }

    @Nested
    @DisplayName("ValidationException Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should throw ValidationException with message")
        void shouldThrowValidationException() {
            // Act & Assert
            assertThatThrownBy(() -> {
                throw new ValidationException("Email format is invalid");
            })
                .isInstanceOf(ValidationException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email format is invalid")
                .hasNoCause();
        }

        @Test
        @DisplayName("Should throw ValidationException with cause")
        void shouldThrowValidationExceptionWithCause() {
            // Arrange
            Exception cause = new IllegalArgumentException("Pattern matching failed");

            // Act & Assert
            assertThatThrownBy(() -> {
                throw new ValidationException("Validation failed", cause);
            })
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed")
                .hasCause(cause);
        }

        @Test
        @DisplayName("Should validate email format")
        void shouldValidateEmailFormat() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateEmail("invalid_email");
            })
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email format is invalid");
        }

        @Test
        @DisplayName("Should validate password strength")
        void shouldValidatePasswordStrength() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validatePassword("123");
            })
                .isInstanceOf(ValidationException.class)
                .hasMessage("Password must be at least 8 characters");
        }

        @Test
        @DisplayName("Should validate required fields")
        void shouldValidateRequiredFields() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateRequired(null, "Username");
            })
                .isInstanceOf(ValidationException.class)
                .hasMessage("Username is required");
        }

        private void validateEmail(String email) {
            if (!email.contains("@")) {
                throw new ValidationException("Email format is invalid");
            }
        }

        private void validatePassword(String password) {
            if (password.length() < 8) {
                throw new ValidationException("Password must be at least 8 characters");
            }
        }

        private void validateRequired(String value, String fieldName) {
            if (value == null || value.trim().isEmpty()) {
                throw new ValidationException(fieldName + " is required");
            }
        }
    }

    @Nested
    @DisplayName("InvalidTransactionException Tests")
    class InvalidTransactionExceptionTests {

        @Test
        @DisplayName("Should throw InvalidTransactionException with message")
        void shouldThrowInvalidTransactionException() {
            // Act & Assert
            assertThatThrownBy(() -> {
                throw new InvalidTransactionException("Cannot transfer to same account");
            })
                .isInstanceOf(InvalidTransactionException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cannot transfer to same account")
                .hasNoCause();
        }

        @Test
        @DisplayName("Should throw InvalidTransactionException with cause")
        void shouldThrowInvalidTransactionExceptionWithCause() {
            // Arrange
            Exception cause = new IllegalStateException("Account validation failed");

            // Act & Assert
            assertThatThrownBy(() -> {
                throw new InvalidTransactionException("Invalid transaction", cause);
            })
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessage("Invalid transaction")
                .hasCause(cause);
        }

        @Test
        @DisplayName("Should validate same account transfer")
        void shouldValidateSameAccountTransfer() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateTransfer(1L, 1L);
            })
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessage("Cannot transfer to same account");
        }

        @Test
        @DisplayName("Should validate negative amount")
        void shouldValidateNegativeAmount() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateTransferAmount(-100.0);
            })
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessage("Transfer amount must be positive");
        }

        @Test
        @DisplayName("Should validate zero amount")
        void shouldValidateZeroAmount() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateTransferAmount(0.0);
            })
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessage("Transfer amount must be positive");
        }

        @Test
        @DisplayName("Should validate maximum transfer limit")
        void shouldValidateMaximumTransferLimit() {
            // Act & Assert
            assertThatThrownBy(() -> {
                validateTransferAmount(1_000_000.0);
            })
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessage("Transfer amount exceeds maximum limit");
        }

        private void validateTransfer(Long sourceId, Long targetId) {
            if (sourceId.equals(targetId)) {
                throw new InvalidTransactionException("Cannot transfer to same account");
            }
        }

        private void validateTransferAmount(Double amount) {
            if (amount <= 0) {
                throw new InvalidTransactionException("Transfer amount must be positive");
            }
            if (amount > 500_000) {
                throw new InvalidTransactionException("Transfer amount exceeds maximum limit");
            }
        }
    }

    @Nested
    @DisplayName("Exception Hierarchy Tests")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("All custom exceptions should extend RuntimeException")
        void allCustomExceptionsShouldExtendRuntimeException() {
            // Arrange
            InsufficientFundsException ex1 = new InsufficientFundsException("test");
            InvalidJwtTokenException ex2 = new InvalidJwtTokenException("test");
            UserNotFoundException ex3 = new UserNotFoundException("test");
            ValidationException ex4 = new ValidationException("test");
            InvalidTransactionException ex5 = new InvalidTransactionException("test");

            // Act & Assert
            assertThat(ex1).isInstanceOf(RuntimeException.class);
            assertThat(ex2).isInstanceOf(RuntimeException.class);
            assertThat(ex3).isInstanceOf(RuntimeException.class);
            assertThat(ex4).isInstanceOf(RuntimeException.class);
            assertThat(ex5).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should be able to catch all exceptions with RuntimeException")
        void shouldBeAbleToCatchAllExceptionsWithRuntimeException() {
            // Act & Assert
            assertThatThrownBy(this::throwFinancialException)
                .isInstanceOf(RuntimeException.class);

            assertThatThrownBy(this::throwAuthenticationException)
                .isInstanceOf(RuntimeException.class);

            assertThatThrownBy(this::throwValidationErrorException)
                .isInstanceOf(RuntimeException.class);
        }

        private void throwFinancialException() {
            throw new InsufficientFundsException("Insufficient funds");
        }

        private void throwAuthenticationException() {
            throw new InvalidJwtTokenException("Invalid token");
        }

        private void throwValidationErrorException() {
            throw new ValidationException("Invalid input");
        }
    }
}
