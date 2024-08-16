package com.example.backend.service;

import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.dataaccess.PortfolioRepository;
import com.example.backend.dataaccess.StockRepository;
import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public Portfolio viewPortfolio(long id) throws IllegalArgumentException {
        Portfolio portfolio = portfolioRepository.findById(id);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio not found");
        }
        return portfolio;
    }

    @Override
    public Portfolio transferToAccount(long id, String nameCode, float amount) throws IllegalArgumentException {
        Portfolio portfolio = portfolioRepository.findById(id);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio not found");
        }

        Account account = accountRepository.findByNameCode(nameCode);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (portfolio.getBalance() < amount) {
            throw new IllegalArgumentException("Transfer amount cannot be more than current account balance");
        }

        portfolio.withdraw(amount);
        account.deposit(amount);

        accountRepository.save(account);

        return portfolioRepository.save(portfolio);
    }

    @Override
    public List<Stock> viewStocks(long id) throws IllegalArgumentException {
        Portfolio portfolio = portfolioRepository.findById(id);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio not found");
        }

        List<Stock> allStocks = new ArrayList<>();
        for (Account account : portfolio.getAccounts()) {
            Set<String> watchListTickers = account.getWatchList();
            if (!watchListTickers.isEmpty()) {
                List<Stock> stocks = stockRepository.findAllByTickerIn(watchListTickers);
                allStocks.addAll(stocks);
            }
        }

        return allStocks;
    }

    @Override
    public List<Investment> viewInvestments(long id) throws IllegalArgumentException {
        Portfolio portfolio = portfolioRepository.findById(id);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio not found");
        }

        List<Investment> allInvestments = new ArrayList<>();
        for (Account account : portfolio.getAccounts()) {
            allInvestments.addAll(account.getInvestments());
        }

        return allInvestments;
    }

    @Override
    public List<Transaction> viewTransactions(long id) throws IllegalArgumentException {
        Portfolio portfolio = portfolioRepository.findById(id);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio not found");
        }

        List<Transaction> allTransactions = new ArrayList<>();
        for (Account account : portfolio.getAccounts()) {
            allTransactions.addAll(account.getTransactions());
        }

        return allTransactions;
    }
}
