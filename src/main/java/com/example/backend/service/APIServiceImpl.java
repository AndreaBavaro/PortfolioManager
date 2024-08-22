package com.example.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIServiceImpl implements APIService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://www.alphavantage.co/query";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public APIServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public JsonNode getStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=5min&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }

    @Override
    public JsonNode getStockOverview(String symbol) throws Exception {
        String url = BASE_URL + "?function=OVERVIEW&symbol=" + symbol + "&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }


    public JsonNode get1MinStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }

    @Override
    public JsonNode get5MinStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=5min&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }

    @Override
    public JsonNode getHourlyStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=60min&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }

    @Override
    public JsonNode getDailyStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }

    @Override
    public JsonNode getWeeklyStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_WEEKLY&symbol=" + symbol + "&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }

    @Override
    public JsonNode getMonthlyStockData(String symbol) throws Exception {
        String url = BASE_URL + "?function=TIME_SERIES_MONTHLY&symbol=" + symbol + "&apikey=" + apiKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(jsonResponse);
    }
}

