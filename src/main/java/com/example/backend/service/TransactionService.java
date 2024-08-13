package com.example.backend.service;

import com.example.backend.model.Transaction;

import java.util.List;

public interface TransactionService {
    public boolean validateTransaction(Transaction transaction);
    public Transaction addTransaction(Transaction transaction);
    public List<Transaction> getAllTransactions();

}
