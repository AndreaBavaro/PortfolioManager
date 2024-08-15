package com.example.backend.service;

import com.example.backend.model.Account;
import com.example.backend.model.Investment;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    public Account createAccount(String nameCode);
    void transferFunds(String fromNameCode, String toNameCode, float amount);
    public Iterable<Account> getAllAccounts();
    public Account viewAccount(String nameCode);
    public List<Stock> viewStocks(String nameCode);
    public List<Investment> viewInvestments(String nameCode);
    public List<Transaction> viewTransactions(String nameCode);
}
