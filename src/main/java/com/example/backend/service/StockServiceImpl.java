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
    public Stock updateStockDataFromAPI(String ticker) throws Exception {
        JsonNode stockData = apiService.get1MinStockData(ticker);
        JsonNode stockOverview = apiService.getStockOverview(ticker);

        if (stockData != null && stockData.has("Time Series (1min)")) {
            JsonNode timeSeries = stockData.get("Time Series (1min)");

            // Get the most recent timestamp and corresponding data
            Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
            if (fields.hasNext()) {
                Map.Entry<String, JsonNode> latestEntry = fields.next();
                String timestampStr = latestEntry.getKey();
                JsonNode latestData = latestEntry.getValue();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, formatter);

                Timestamp timestamp = Timestamp.valueOf(localDateTime);

                // Fetch or create a new stock object
                Stock stock = stockRepository.findById(ticker).orElse(new Stock());
                if (stock.getTimestamp() == null || timestamp.after(stock.getTimestamp())) {
                    // Always update the stock with the latest data
                    Long price = latestData.get("4. close").asLong();
                    Long high = latestData.get("2. high").asLong();
                    Long low = latestData.get("3. low").asLong();
                    Long volume = latestData.get("5. volume").asLong();

                    String name = stockOverview.has("Name") ? stockOverview.get("Name").asText() : "Unknown";

                    stock.setTicker(ticker);
                    stock.setName(name);
                    stock.setPrice(price);
                    stock.setHigh(high);
                    stock.setLow(low);
                    stock.setVol(volume);
                    stock.setTimestamp(timestamp);

                    return stockRepository.save(stock);
                } else {
                    System.out.println("No update needed. Stored data is more recent or equal.");
                }
            }
        }
        return null;
    }

    @Scheduled(fixedRate = 300000)
    public void updateAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            try {
                updateStockDataFromAPI(stock.getTicker());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public JsonNode get1MinStockData(String ticker) throws Exception {
        return apiService.get1MinStockData(ticker);
    }

    @Override
    public JsonNode get5MinStockData(String ticker) throws Exception {
        return apiService.get5MinStockData(ticker);
    }

    @Override
    public JsonNode getHourlyStockData(String ticker) throws Exception {
        return apiService.getHourlyStockData(ticker);
    }

    @Override
    public JsonNode getDailyStockData(String ticker) throws Exception {
        return apiService.getDailyStockData(ticker);
    }

    @Override
    public JsonNode getWeeklyStockData(String ticker) throws Exception {
        return apiService.getWeeklyStockData(ticker);
    }

    @Override
    public JsonNode getMonthlyStockData(String ticker) throws Exception {
        return apiService.getMonthlyStockData(ticker);
    }

    @Override
    public void saveSimulatedData(String ticker, LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        LocalDateTime currentTime = startTime;
        while (!currentTime.isAfter(endTime)) {
            JsonNode stockData = apiService.get1MinStockData(ticker);
            // Assuming the timestamp in the response matches the currentTime or can be parsed accordingly
            if (stockData != null && stockData.has("Time Series (1min)")) {
                JsonNode timeSeries = stockData.get("Time Series (1min)");
                Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
                if (fields.hasNext()) {
                    Map.Entry<String, JsonNode> latestEntry = fields.next();
                    String timestampStr = latestEntry.getKey();
                    JsonNode latestData = latestEntry.getValue();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, formatter);
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);

                    Long price = latestData.get("4. close").asLong();
                    Long high = latestData.get("2. high").asLong();
                    Long low = latestData.get("3. low").asLong();
                    Long volume = latestData.get("5. volume").asLong();

                    Stock stock = stockRepository.findById(ticker).orElse(new Stock());
                    stock.setTicker(ticker);
                    stock.setPrice(price);
                    stock.setHigh(high);
                    stock.setLow(low);
                    stock.setVol(volume);
                    stock.setTimestamp(timestamp);

                    stockRepository.save(stock);
                }
            }
            currentTime = currentTime.plusMinutes(1);
        }
    }
}
