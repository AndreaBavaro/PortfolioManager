package com.example.backend.service;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.dataaccess.PortfolioRepository;
import com.example.backend.dataaccess.StockRepository;
import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public void transferFunds(String fromNameCode, String toNameCode, float amount) throws ResourceNotFoundException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        // Fetch the source account
        Account fromAccount = accountRepository.findByNameCode(fromNameCode);
        if (fromAccount == null) {
            throw new ResourceNotFoundException("Source Account not found");
        }

        // Fetch the destination account
        Account toAccount = accountRepository.findByNameCode(toNameCode);
        if (toAccount == null) {
            throw new ResourceNotFoundException("Destination Account not found");
        }

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Transfer amount cannot be more than current account balance");
        }

        // Perform the fund transfer
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        // Save the updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
    @Override
    public Account createAccount(String nameCode, String accountType, float balance, Portfolio portfolio) throws IllegalArgumentException {
        Account account = new Account(nameCode, accountType, balance, portfolio);
        portfolio.addAccount(account);
        portfolioRepository.save(portfolio); // Save the portfolio with the new account
        return account;
    }

    @Override
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account viewAccount(String nameCode) throws ResourceNotFoundException {
        Account account = accountRepository.findByNameCode(nameCode);
        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }
        return account;
    }

    @Override
    public List<Stock> viewWatchlist(String nameCode) {
        return accountRepository.viewWatchlistByAccount(nameCode);
    }
    @Override
    public List<Investment> viewInvestments(String nameCode) {
        return accountRepository.findAllInvestmentsByAccount(nameCode);
    }
    @Override
    public List<Transaction> viewTransactions(String nameCode) {
        return accountRepository.findAllTransactionsByAccount(nameCode);
    }

    @Override
    public Account addToWatchList(String nameCode, Stock ticker) {
        Account account = accountRepository.findByNameCode(nameCode);
        account.addToWatchList(ticker);
        return accountRepository.save(account);
    }
    @Override
    public Account removeFromWatchList(String nameCode, Stock ticker) {
        Account account = accountRepository.findByNameCode(nameCode);
        account.removeFromWatchList(ticker);
        return accountRepository.save(account);
    }

}
