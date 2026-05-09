package com.transactionguard.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public enum TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED,
    CANCELLED
}
