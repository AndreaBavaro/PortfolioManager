package com.example.backend.service;

import com.example.backend.dataaccess.TransactionRepository;
import com.example.backend.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransactionServiceImpl implements  TransactionService {
    TransactionRepository transactionRepository;

    @Override
    public boolean validateTransaction(Transaction transaction) {
        return false;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return List.of();
    }
    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }
}
