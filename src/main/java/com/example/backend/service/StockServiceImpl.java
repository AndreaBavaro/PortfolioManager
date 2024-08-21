package com.example.backend.service;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.dataaccess.StockRepository;
import com.example.backend.model.Stock;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private StockRepository stockRepository;
    private APIService apiService;

    @Autowired
    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Autowired
    public void setApiService(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Iterable<Stock> findAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock viewStock(String ticker) {
        Optional<Stock> stock = stockRepository.findById(ticker);
        if (stock.isPresent()) {
            return stock.get();
        } else {
            throw new ResourceNotFoundException("Invalid Ticker");
        }
    }

    @Override
    public Stock findStock(String ticker) {
        return stockRepository.findByTicker(ticker);
    }

    @Override
    public void deleteStock(Stock ticker) {
        stockRepository.delete(ticker);
    }

    @Override
    public Stock updateStockDataFromAPI(String ticker) {
        try {
            JsonNode stockData = apiService.getStockData(ticker);
            JsonNode stockOverview = apiService.getStockOverview(ticker);  // Fetch stock overview to get the name

            if (stockData != null && stockData.has("Time Series (5min)")) {
                JsonNode timeSeries = stockData.get("Time Series (5min)");

                // Get the latest timestamp and corresponding data
                Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
                if (fields.hasNext()) {
                    Map.Entry<String, JsonNode> latestEntry = fields.next();
                    String timestampStr = latestEntry.getKey();
                    JsonNode latestData = latestEntry.getValue();

                    Long price = latestData.get("4. close").asLong();
                    Long high = latestData.get("2. high").asLong();
                    Long low = latestData.get("3. low").asLong();
                    Long volume = latestData.get("5. volume").asLong();

                    // Parse the timestamp string (format 'yyyy-MM-dd HH:mm:ss')
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, formatter);

                    // Convert LocalDateTime to java.sql.Timestamp
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);

                    // Extract the stock name from the overview response
                    String name = stockOverview.has("Name") ? stockOverview.get("Name").asText() : "Unknown";

                    Stock stock = stockRepository.findById(ticker).orElse(new Stock());
                    stock.setTicker(ticker);
                    stock.setName(name); // Set the name of the stock
                    stock.setPrice(price);
                    stock.setHigh(high);
                    stock.setLow(low);
                    stock.setVol(volume);
                    stock.setTimestamp(timestamp);

                    return stockRepository.save(stock);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Scheduled method to update all stocks periodically
    @Scheduled(fixedRate = 300000) // Runs every 5 minutes (300,000 milliseconds)
    public void updateAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            updateStockDataFromAPI(stock.getTicker());
        }
    }
}
