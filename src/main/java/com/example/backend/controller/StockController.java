package com.example.backend.controller;

import com.example.backend.model.Stock;
import com.example.backend.service.StockService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

            if (stock == null) {
                // Stock not found in the database, fetch it from the API
                stock = stockService.updateStockDataFromAPI(ticker);
                if (stock != null) {
                    return new ResponseEntity<>(stock, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

            // Stock found, update data even if it is outdated
            stock = stockService.updateStockDataFromAPI(ticker);
            return new ResponseEntity<>(stock, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get 1-minute interval data
    @GetMapping("/stock/{ticker}/1min")
    public ResponseEntity<JsonNode> get1MinStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.get1MinStockData(ticker);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get 5-minute interval data
    @GetMapping("/stock/{ticker}/5min")
    public ResponseEntity<JsonNode> get5MinStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.get5MinStockData(ticker);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get hourly interval data
    @GetMapping("/stock/{ticker}/hourly")
    public ResponseEntity<JsonNode> getHourlyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getHourlyStockData(ticker);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get daily interval data
    @GetMapping("/stock/{ticker}/daily")
    public ResponseEntity<JsonNode> getDailyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getDailyStockData(ticker);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get weekly interval data
    @GetMapping("/stock/{ticker}/weekly")
    public ResponseEntity<JsonNode> getWeeklyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getWeeklyStockData(ticker);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get monthly interval data
    @GetMapping("/stock/{ticker}/monthly")
    public ResponseEntity<JsonNode> getMonthlyStockData(@PathVariable String ticker) {
        try {
            JsonNode data = stockService.getMonthlyStockData(ticker);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
