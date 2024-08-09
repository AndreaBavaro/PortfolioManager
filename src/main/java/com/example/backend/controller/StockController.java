package com.example.backend.controller;

import com.example.backend.model.Stock;
import com.example.backend.service.AccountService;
import com.example.backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    StockService stockService;

    @GetMapping("/stock")
    public Iterable<Stock> viewAllStock() {return stockService.findAllStock();}


    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }
}
