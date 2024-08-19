package com.example.backend.controller;

import com.example.backend.model.Transaction;
import com.example.backend.service.InvestmentService;
import com.example.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class TransactionController {

    TransactionService transactionService;

    @GetMapping("/transaction")
    public Iterable<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/transaction/buy")
    public Transaction buyTransaction(@RequestBody Transaction transaction) {
        return transactionService.buyTransaction(transaction);
    }

    @PostMapping ("/transaction/sell")
    public Transaction sellTransaction(@RequestBody Transaction transaction) {
        return transactionService.sellTransaction(transaction);
    }

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
