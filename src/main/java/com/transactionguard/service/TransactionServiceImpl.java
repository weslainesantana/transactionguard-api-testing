package com.transactionguard.service;

import com.transactionguard.domain.Transaction;
import com.transactionguard.domain.TransactionStatus;
import com.transactionguard.dto.TransactionRequest;
import com.transactionguard.exception.InsufficientFundsException;
import com.transactionguard.repository.TransactionRepository;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction transfer(TransactionRequest request) {
        // Validações
        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (request.getSourceAccountId().equals(request.getTargetAccountId())) {
            throw new IllegalArgumentException("Source and target accounts cannot be the same");
        }

        // Verificar saldo (simplificado - em produção seria mais complexo)
        if (request.getAmount().compareTo(java.math.BigDecimal.valueOf(5000)) > 0) {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }

        // Criar e salvar transação
        Transaction transaction = new Transaction(
            request.getSourceAccountId(),
            request.getTargetAccountId(),
            request.getAmount(),
            TransactionStatus.SUCCESS
        );

        return repository.save(transaction);
    }

    @Override
    public Transaction getTransaction(Long id) {
        return repository.findById(id).orElse(null);
    }
}
