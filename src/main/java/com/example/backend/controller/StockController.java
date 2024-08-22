package com.example.backend.controller;

import com.example.backend.model.Stock;
import com.example.backend.service.StockService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stock")
    public ResponseEntity<Iterable<Stock>> getAllStocks() {
        try {
            Iterable<Stock> stocks = stockService.findAllStock();
            return new ResponseEntity<>(stocks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stock/{ticker}")
    public ResponseEntity<Stock> getStock(@PathVariable String ticker) {
        try {
            Stock stock = stockService.findStock(ticker);

            if (stock == null || stock.getTicker() == null || stock.getTicker().isEmpty() || stock.getPrice() <= 0) {
                // Stock not found in the database or stock object is invalid, fetch it from the API
                stock = stockService.updateStockDataFromAPI(ticker);
                if (stock != null && stock.getTicker() != null && !stock.getTicker().isEmpty() && stock.getPrice() > 0) {
                    return new ResponseEntity<>(stock, HttpStatus.OK);
                } else if (stock == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }

            // Stock found, update data even if it is outdated
            Stock updatedStock = stockService.updateStockDataFromAPI(ticker);
            if (updatedStock != null && updatedStock.getTicker() != null && !updatedStock.getTicker().isEmpty() && updatedStock.getPrice() > 0) {
                return new ResponseEntity<>(updatedStock, HttpStatus.OK);
            }

            return new ResponseEntity<>(stock, HttpStatus.OK); // Return the found stock if API fails

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Map<String, String>> parseTimeSeries(JsonNode timeSeries) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        if (timeSeries != null) {
            for (Iterator<Map.Entry<String, JsonNode>> it = timeSeries.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                Map<String, String> stockData = new HashMap<>();
                stockData.put("timestamp", entry.getKey());
                stockData.put("close", entry.getValue().get("4. close").asText());
                result.add(stockData);
            }
        }
        return result;
    }

    // Get 1-minute interval data
    @GetMapping("/stock/{ticker}/1min")
    public ResponseEntity<List<Map<String, String>>> get1MinStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.get1MinStockData(ticker);
            JsonNode timeSeries = data.get("Time Series (1min)");
            List<Map<String, String>> result = parseTimeSeries(timeSeries);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get 5-minute interval data
    @GetMapping("/stock/{ticker}/5min")
    public ResponseEntity<List<Map<String, String>>> get5MinStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.get5MinStockData(ticker);
            JsonNode timeSeries = data.get("Time Series (5min)");
            List<Map<String, String>> result = parseTimeSeries(timeSeries);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get hourly interval data
    @GetMapping("/stock/{ticker}/hourly")
    public ResponseEntity<List<Map<String, String>>> getHourlyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getHourlyStockData(ticker);
            JsonNode timeSeries = data.get("Time Series (60min)");
            List<Map<String, String>> result = parseTimeSeries(timeSeries);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get daily interval data
    @GetMapping("/stock/{ticker}/daily")
    public ResponseEntity<List<Map<String, String>>> getDailyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getDailyStockData(ticker);
            JsonNode timeSeries = data.get("Time Series (Daily)");
            List<Map<String, String>> result = parseTimeSeries(timeSeries);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get weekly interval data
    @GetMapping("/stock/{ticker}/weekly")
    public ResponseEntity<List<Map<String, String>>> getWeeklyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getWeeklyStockData(ticker);
            JsonNode timeSeries = data.get("Weekly Time Series");
            List<Map<String, String>> result = parseTimeSeries(timeSeries);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get monthly interval data
    @GetMapping("/stock/{ticker}/monthly")
    public ResponseEntity<List<Map<String, String>>> getMonthlyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getMonthlyStockData(ticker);
            JsonNode timeSeries = data.get("Monthly Time Series");
            List<Map<String, String>> result = parseTimeSeries(timeSeries);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stock/{ticker}/simulate")
    public ResponseEntity<String> simulateStockData(@PathVariable String ticker) {
        try {
            LocalDateTime startTime = LocalDateTime.of(2024, 8, 19, 9, 0);  // Example: 9:00 AM yesterday
            LocalDateTime endTime = LocalDateTime.of(2024, 8, 19, 16, 0);   // Example: 4:00 PM yesterday
            stockService.saveSimulatedData(ticker, startTime, endTime);
            return new ResponseEntity<>("Simulation completed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
