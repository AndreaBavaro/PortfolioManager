package com.example.backend.controller;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.model.*;
import com.example.backend.service.AccountService;
import com.example.backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    PortfolioService portfolioService;



    @GetMapping
    public ResponseEntity<Iterable<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{nameCode}")
    public ResponseEntity<Account> viewAccount(@PathVariable String nameCode) {
        try {
            Account account = accountService.viewAccount(nameCode);
            return ResponseEntity.ok(account);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            account = accountService.createAccount(account.getNameCode(), account.getAccountType(), account.getBalance(), account.getPortfolio());
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{fromNameCode}/transfer/{toNameCode}")
    public ResponseEntity<Void> transferFunds(@PathVariable String fromNameCode,
                                              @PathVariable String toNameCode,
                                              @RequestParam float amount) {
        try {
            accountService.transferFunds(fromNameCode, toNameCode, amount);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{nameCode}/stocks")
    public ResponseEntity<List<Stock>> viewStocks(@PathVariable String nameCode) {
        return ResponseEntity.ok(accountService.viewWatchlist(nameCode));
    }

    @GetMapping("/{nameCode}/investments")
    public ResponseEntity<List<Investment>> viewInvestments(@PathVariable String nameCode) {
        return ResponseEntity.ok(accountService.viewInvestments(nameCode));
    }

    @GetMapping("/{nameCode}/transactions")
    public ResponseEntity<List<Transaction>> viewTransactions(@PathVariable String nameCode) {
        return ResponseEntity.ok(accountService.viewTransactions(nameCode));
    }

    @GetMapping("/{nameCode}/watchlist")
    public ResponseEntity<List<Stock>> viewWatchList(@PathVariable String nameCode) {
        return ResponseEntity.ok(accountService.viewWatchlist(nameCode));
    }

    @PostMapping("/{nameCode}/watchlist/{ticker}")
    public ResponseEntity<Account> addToWatchList(@PathVariable String nameCode,
                                                  @PathVariable String ticker) {
        return ResponseEntity.ok(accountService.addToWatchList(nameCode, ticker));
    }

    @DeleteMapping("/{nameCode}/watchlist/{ticker}")
    public ResponseEntity<Account> removeFromWatchList(@PathVariable String nameCode,
                                                       @PathVariable String ticker) {
        return ResponseEntity.ok(accountService.removeFromWatchList(nameCode, ticker));
    }
}
