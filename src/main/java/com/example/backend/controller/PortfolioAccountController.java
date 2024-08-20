package com.example.backend.controller;

import com.example.backend.ResourceNotFoundException;
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
public class PortfolioAccountController {

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

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<Account>> getAllAccountsByPortfolio(@PathVariable Long id) {
        List<Account> accounts = portfolioService.viewAccounts(id);
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return a 204 No Content if no accounts are found
        }
        return ResponseEntity.ok(accounts);
    }


    @GetMapping("/{id}/account/{nameCode}")
    public ResponseEntity<Account> viewAccount(@PathVariable Long id, @PathVariable String nameCode) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(account);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Account> createAccount(@PathVariable Long id, @RequestBody Account account) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            account.setPortfolio(portfolio);
            account = accountService.createAccount(
                    account.getNameCode(),
                    account.getAccountType(),
                    account.getBalance(),
                    portfolio
            );
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/account/{fromNameCode}/transfer/{toNameCode}")
    public ResponseEntity<Void> transferFunds(@PathVariable Long id,
                                              @PathVariable String fromNameCode,
                                              @PathVariable String toNameCode,
                                              @RequestParam float amount) {
        System.out.println("Received request to transfer funds: id=" + id + ", fromNameCode=" + fromNameCode + ", toNameCode=" + toNameCode + ", amount=" + amount);

        try {
            System.out.println("Fetching portfolio with id=" + id);
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            System.out.println("Fetched portfolio: " + portfolio);

            System.out.println("Fetching from account with nameCode=" + fromNameCode);
            Account fromAccount = accountService.viewAccount(fromNameCode);
            System.out.println("Fetched from account: " + fromAccount);

            System.out.println("Fetching to account with nameCode=" + toNameCode);
            Account toAccount = accountService.viewAccount(toNameCode);
            System.out.println("Fetched to account: " + toAccount);

            if (fromAccount.getPortfolio().getId() != id || toAccount.getPortfolio().getId() != id) {
                System.out.println("Validation failed: accounts do not belong to the specified portfolio");
                return ResponseEntity.badRequest().build();
            }

            System.out.println("Transferring " + amount + " from " + fromNameCode + " to " + toNameCode);
            accountService.transferFunds(fromNameCode, toNameCode, amount);
            System.out.println("Transfer successful");

            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}/account/{nameCode}/investments")
    public ResponseEntity<List<Investment>> viewInvestments(@PathVariable Long id, @PathVariable String nameCode) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(accountService.viewInvestments(nameCode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/account/{nameCode}/transactions")
    public ResponseEntity<List<Transaction>> viewTransactions(@PathVariable Long id, @PathVariable String nameCode) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(accountService.viewTransactions(nameCode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/account/{nameCode}/watchlist")
    public ResponseEntity<List<Stock>> viewWatchList(@PathVariable Long id, @PathVariable String nameCode) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(accountService.viewWatchlist(nameCode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/account/{nameCode}/watchlist/add/{ticker}")
    public ResponseEntity<Account> addToWatchList(@PathVariable Long id, @PathVariable String nameCode,
                                                  @PathVariable Stock ticker) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(accountService.addToWatchList(nameCode, ticker));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/account/{nameCode}/watchlist/delete/{ticker}")
    public ResponseEntity<Account> removeFromWatchList(@PathVariable Long id, @PathVariable String nameCode,
                                                       @PathVariable Stock ticker) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(accountService.removeFromWatchList(nameCode, ticker));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
