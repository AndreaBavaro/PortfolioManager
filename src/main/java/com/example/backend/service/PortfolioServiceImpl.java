package com.example.backend.service;

import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.dataaccess.PortfolioRepository;
import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class PortfolioServiceImpl implements PortfolioService {

    PortfolioRepository portfolioRepository;
    AccountRepository accountRepository;

    @Override
    public Portfolio viewPortfolio(long id) {
        return portfolioRepository.findById(id);
    }



    @Override
    public void transferToAccount(long id, String nameCode, float amount) {
        Portfolio portfolio = portfolioRepository.findById(id);
        Account account = accountRepository.findByNameCode(nameCode);
        if(portfolio.getBalance() < amount ){
            throw new IllegalArgumentException("Transfer amount cannot be more than current account balance");
        }
        portfolio.withdraw(amount);
        account.deposit(amount);

        accountRepository.save(account);
        portfolioRepository.save(portfolio);
    }

    @Override
    public List<Stock> viewStocks(long id) {
        return List.of();
    }

    @Override
    public List<Investment> viewInvestments(long id) {
        return List.of();
    }

    @Override
    public List<Transaction> viewTransactions(long id) {
        return List.of();
    }

    @Autowired
    public void setPortfolioRepository(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }
    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
