/***

package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.service.AccountService;
import com.example.backend.service.PortfolioService;
import com.example.backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    PortfolioService portfolioService;
    @Autowired
    AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> viewPortfolio(@PathVariable long id) {
        Portfolio portfolio = portfolioService.viewPortfolio(id);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/{id}/watchlist")
    public ResponseEntity<List<Stock>> viewWatchlist(@PathVariable long id) {
        List<Stock> stocks = portfolioService.viewStocks(id);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{id}/investments")
    public ResponseEntity<List<Investment>> viewInvestments(@PathVariable long id) {
        List<Investment> investments = portfolioService.viewInvestments(id);
        return ResponseEntity.ok(investments);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> viewTransactions(@PathVariable long id) {
        List<Transaction> transactions = portfolioService.viewTransactions(id);
        return ResponseEntity.ok(transactions);
    }
    @PutMapping("/{id}/{accountCode}/transfer/")
    public ResponseEntity<Portfolio> transferFunds(@PathVariable long id, @PathVariable String accountCode, @RequestParam float amount){
        Portfolio portfolio = portfolioService.transferToAccount(id, accountCode, amount);
        return ResponseEntity.ok(portfolio);
    }


}
***/