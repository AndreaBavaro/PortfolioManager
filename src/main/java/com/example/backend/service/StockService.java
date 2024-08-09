package com.example.backend.service;

import com.example.backend.model.Stock;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    public Stock saveStock(Stock stock);
    public Iterable<Stock> findAllStock();
}
