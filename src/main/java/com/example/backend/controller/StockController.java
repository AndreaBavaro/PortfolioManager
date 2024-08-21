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

            if (stock == null) {
                // Stock not found in the database, fetch it from the API
                stock = stockService.updateStockDataFromAPI(ticker);
                if (stock != null) {
                    return new ResponseEntity<>(stock, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

            // Stock found, check if it needs updating
            Timestamp lastUpdated = stock.getTimestamp();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            long millisecondsSinceLastUpdate = currentTime.getTime() - lastUpdated.getTime();
            long minutesSinceLastUpdate = millisecondsSinceLastUpdate / (1000 * 60);

            if (minutesSinceLastUpdate >= 5) {
                stock = stockService.updateStockDataFromAPI(ticker); // Update if 5 minutes have passed
            } else {
                // If the stock is found and within the time limit, still update the timestamp
                stock.setTimestamp(currentTime);
                stockService.saveStock(stock);
            }

            return new ResponseEntity<>(stock, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
