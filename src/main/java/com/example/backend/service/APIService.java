package com.example.backend.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface APIService {
    JsonNode getStockData(String symbol) throws Exception;
    JsonNode getStockOverview(String symbol) throws Exception;
    JsonNode getHourlyStockData(String symbol) throws Exception;
    JsonNode getDailyStockData(String symbol) throws Exception;
    JsonNode getWeeklyStockData(String symbol) throws Exception;
    JsonNode getMonthlyStockData(String symbol) throws Exception;

    JsonNode get1MinStockData(String symbol) throws Exception;

    JsonNode get5MinStockData(String symbol) throws Exception;
}

