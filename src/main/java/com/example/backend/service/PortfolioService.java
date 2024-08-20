package com.example.backend.service;
import com.example.backend.model.*;

import java.util.List;

public interface PortfolioService {
    public Portfolio viewPortfolio(long id);

    List<Account> viewAccounts(long id) throws IllegalArgumentException;

    public Portfolio transferToAccount(long id, String nameCode, float amount);
    public List<Stock> viewStocks(long id);
    public List<Investment> viewInvestments(long id);
    public List<Transaction> viewTransactions(long id);

}
