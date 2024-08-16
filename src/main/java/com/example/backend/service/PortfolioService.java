package com.example.backend.service;
import com.example.backend.model.Investment;
import com.example.backend.model.Portfolio;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;

import java.util.List;

public interface PortfolioService {
    public Portfolio viewPortfolio(long id);
    public Portfolio transferToAccount(long id, String nameCode, float amount);
    public List<Stock> viewStocks(long id);
    public List<Investment> viewInvestments(long id);
    public List<Transaction> viewTransactions(long id);

}
