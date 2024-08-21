package com.example.backend.controller;

import com.example.backend.model.Stock;
import com.example.backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stock")
    public ResponseEntity<Iterable<Stock>> getAllStocks() {
        Iterable<Stock> stocks = stockService.findAllStock();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @GetMapping("/stock/{ticker}")
    public ResponseEntity<Stock> getStock(@PathVariable String ticker) {
        try {
            Stock stock = stockService.findStock(ticker);
            if (stock != null) {
                // Check if 5 minutes have passed since the last update
                Timestamp lastUpdated = stock.getTimestamp();
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                long millisecondsSinceLastUpdate = currentTime.getTime() - lastUpdated.getTime();
                long minutesSinceLastUpdate = millisecondsSinceLastUpdate / (1000 * 60);

                if (minutesSinceLastUpdate >= 5) {
                    stock = stockService.updateStockDataFromAPI(ticker); // Update only if 5 minutes have passed
                }

                return new ResponseEntity<>(stock, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
