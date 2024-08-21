package com.example.backend.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface APIService {
    JsonNode getStockData(String symbol) throws Exception;

    JsonNode getStockOverview(String symbol) throws Exception;
}
