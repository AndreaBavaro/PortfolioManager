package com.example.backend.service;

import com.example.backend.model.Stock;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface StockService {
    Stock findStock(String ticker);
    void deleteStock(Stock ticker);
    Stock saveStock(Stock ticker);
    Iterable<Stock> findAllStock();
    Stock viewStock(String ticker);
    Stock updateStockDataFromAPI(String ticker) throws Exception;

    JsonNode get1MinStockData(String ticker) throws Exception;
    JsonNode get5MinStockData(String ticker) throws Exception;
    JsonNode getHourlyStockData(String ticker) throws Exception;
    JsonNode getDailyStockData(String ticker) throws Exception;
    JsonNode getWeeklyStockData(String ticker) throws Exception;
    JsonNode getMonthlyStockData(String ticker) throws Exception;
    void saveSimulatedData(String ticker, LocalDateTime startTime, LocalDateTime endTime) throws Exception;
}
