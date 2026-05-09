package com.transactionguard.repository;

import com.transactionguard.domain.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findBySourceAccountId(Long sourceAccountId);

    List<Transaction> findByTargetAccountId(Long targetAccountId);

    void deleteById(Long id);
}
