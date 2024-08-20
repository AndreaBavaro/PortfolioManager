package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    void transferFunds(String fromNameCode, String toNameCode, float amount);
    public Account createAccount(String nameCode, String accountType, float balance, Portfolio portfolio) throws IllegalArgumentException;
    public Iterable<Account> getAllAccounts();
    public Account viewAccount(String nameCode);
    public List<Stock> viewWatchlist(String nameCode);
    public List<Investment> viewInvestments(String nameCode);
    public List<Transaction> viewTransactions(String nameCode);
    public Account addToWatchList (String nameCode, Stock ticker);
    Account removeFromWatchList(String nameCode, Stock ticker);
}
