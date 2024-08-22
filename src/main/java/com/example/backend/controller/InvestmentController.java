package com.example.backend.controller;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.model.Account;
import com.example.backend.model.Investment;
import com.example.backend.model.Portfolio;
import com.example.backend.service.AccountService;
import com.example.backend.service.InvestmentService;
import com.example.backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class InvestmentController {

    @Autowired
    InvestmentService investmentService;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    AccountService accountService;

    @GetMapping("/{id}/account/{nameCode}/investments")
    public ResponseEntity<List<Investment>> viewInvestments(@PathVariable("id") Long id,
                                                            @PathVariable("nameCode") String nameCode) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(id);
            Account account = accountService.viewAccount(nameCode);
            if (account.getPortfolio().getId() != id) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(accountService.viewInvestments(nameCode));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/account/{nameCode}/investments/{ticker}")
    public ResponseEntity<Investment> findInvestmentByTicker(@PathVariable("id") String id,
                                             @PathVariable("nameCode") String nameCode,
                                             @PathVariable("ticker") String ticker) {
        try {
            Investment investment = investmentService.findInvestmentByTicker(ticker);
            if (investment.getAccount().getNameCode().equals(nameCode)) {
                return ResponseEntity.ok(investment);
            } else {
                throw new ResourceNotFoundException("Investment Not Found in Account");
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
