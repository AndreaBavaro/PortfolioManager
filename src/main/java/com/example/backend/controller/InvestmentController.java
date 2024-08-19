package com.example.backend.controller;

import com.example.backend.model.Investment;
import com.example.backend.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class InvestmentController {

    InvestmentService investmentService;

    @GetMapping("/investment")
    public List<Investment> getALlInvestments() {
        return investmentService.getAllInvestments();
    }

    @GetMapping("/investment/{ticker}")
    public Investment findInvestmentByTicker(@PathVariable String ticker) {
        return investmentService.findInvestmentByTicker(ticker);
    }

    @Autowired
    public void setInvestmentService(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }
}
