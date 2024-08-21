package com.example.backend.controller;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.model.Account;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;
import com.example.backend.service.AccountService;
import com.example.backend.service.StockService;
import com.example.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    StockService stockService;

    @PostMapping("/{id}/account/{nameCode}/transaction/{ticker}/buy")
    public ResponseEntity<Transaction> buyTransaction(@PathVariable("id") Long id,
                                      @PathVariable("nameCode") String nameCode,
                                      @PathVariable("ticker") String ticker,
                                      @RequestBody Transaction transaction) {
        try{
            Account account = accountService.viewAccount(nameCode);
            Stock stock = stockService.viewStock(ticker);
            String type = "BUY";
            transaction = transactionService.buyTransaction(type, transaction.getAmount(), account, stock);
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    @PostMapping("/{id}/account/{nameCode}/transaction/{ticker}/sell")
    public ResponseEntity<Transaction> sellTransaction(@PathVariable("id") Long id,
                                       @PathVariable("nameCode") String nameCode,
                                       @PathVariable("ticker") String ticker,
                                       @RequestBody Transaction transaction) {
        try{
            Account account = accountService.viewAccount(nameCode);
            Stock stock = stockService.viewStock(ticker);
            String type = "SELL";
            transaction = transactionService.sellTransaction(type, transaction.getAmount(), account, stock);
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
