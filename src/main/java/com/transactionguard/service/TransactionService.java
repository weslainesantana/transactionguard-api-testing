package com.transactionguard.service;

import com.transactionguard.domain.Transaction;
import com.transactionguard.dto.TransactionRequest;
import com.transactionguard.exception.InsufficientFundsException;

public interface TransactionService {
    Transaction transfer(TransactionRequest request);

    Transaction getTransaction(Long id);
}
