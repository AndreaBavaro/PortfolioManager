package com.example.backend.service;

import com.example.backend.model.Stock;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    Stock findStock(String ticker);
    void deleteStock(Stock ticker);
    Stock saveStock(Stock ticker);
    Iterable<Stock> findAllStock();
    Stock viewStock(String ticker);  // Retain the viewStock method
    Stock updateStockDataFromAPI(String ticker);  // Retain the updateStockDataFromAPI method
}
