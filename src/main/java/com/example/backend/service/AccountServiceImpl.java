package com.example.backend.service;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.dataaccess.PortfolioRepository;
import com.example.backend.dataaccess.StockRepository;
import com.example.backend.model.Account;
import com.example.backend.model.Investment;
import com.example.backend.model.Portfolio;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public List<Stock> viewStocks(String nameCode) {
        return accountRepository.findAllStocksByAccount(nameCode);
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
    public Account addToWatchList(String nameCode, String ticker) {
        Account account = accountRepository.findByNameCode(nameCode);
        account.addToWatchList(ticker);
        return accountRepository.save(account);
    }
    @Override
    public Account removeFromWatchList(String nameCode, String ticker) {
        Account account = accountRepository.findByNameCode(nameCode);
        account.removeFromWatchList(ticker);
        return accountRepository.save(account);
    }
    public List<Stock> viewWatchList(String nameCode) {
        // Retrieve the account using the nameCode
        Account account = accountRepository.findByNameCode(nameCode);

        if (account != null) {
            // Get the set of stock tickers from the watchList
            Set<String> watchListTickers = account.getWatchList();

            // Fetch the corresponding Stock objects from the StockRepository

            return stockRepository.findAllByTickerIn(watchListTickers);
        }

        // Return an empty list if account is not found or watchlist is empty
        return new ArrayList<>();
    }

}
