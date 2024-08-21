package com.example.backend.service;

import com.example.backend.model.Account;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionService {
    public Transaction buyTransaction(String type, int amount, Account account, Stock stock);
    public Transaction sellTransaction(String type, int amount, Account account, Stock stock);
    public List<Transaction> getAllTransactions();
}
