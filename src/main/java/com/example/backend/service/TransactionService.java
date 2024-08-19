package com.example.backend.service;

import com.example.backend.model.Transaction;

import java.util.List;

public interface TransactionService {
    public Transaction buyTransaction(Transaction transaction);
    public Transaction sellTransaction(Transaction transaction);
    public List<Transaction> getAllTransactions();
}
