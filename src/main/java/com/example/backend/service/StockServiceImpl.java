package com.example.backend.service;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.dataaccess.StockRepository;
import com.example.backend.model.Stock;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class StockServiceImpl implements StockService {
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);


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

    @Override
    public Double calculatePercentageChange(String ticker, String interval) throws Exception {
        JsonNode timeSeries = null;
        String timeSeriesField = "";
        int numEntries = 0;

        logger.info("Calculating percentage change for ticker: {}, interval: {}", ticker, interval);

        switch (interval.toLowerCase()) {
            case "1min":
                timeSeries = get1MinStockData(ticker);
                timeSeriesField = "Time Series (1min)";
                numEntries = 2;
                break;
            case "5min":
                timeSeries = get1MinStockData(ticker);
                timeSeriesField = "Time Series (5min)";
                numEntries = 5;
                break;
            case "15min":
                timeSeries = get5MinStockData(ticker);
                timeSeriesField = "Time Series (5min)";
                numEntries = 3;
                break;
            case "30min":
                timeSeries = get5MinStockData(ticker);
                timeSeriesField = "Time Series (5min)";
                numEntries = 6;
                break;
            case "1hr":
                timeSeries = getHourlyStockData(ticker);
                timeSeriesField = "Time Series (60min)";
                numEntries = 2;
                break;
            case "4hr":
                timeSeries = getHourlyStockData(ticker);
                timeSeriesField = "Time Series (60min)";
                numEntries = 4;
                break;
            case "24hr":
                timeSeries = getDailyStockData(ticker);
                timeSeriesField = "Time Series (Daily)";
                numEntries = 2;
                break;
            case "1week":
                timeSeries = getWeeklyStockData(ticker);
                timeSeriesField = "Weekly Time Series";
                numEntries = 2;
                break;
            case "1month":
                timeSeries = getMonthlyStockData(ticker);
                timeSeriesField = "Monthly Time Series";
                numEntries = 2;
                break;
            case "1year":
                timeSeries = getMonthlyStockData(ticker);
                timeSeriesField = "Monthly Time Series";
                numEntries = 12;
                break;
            default:
                logger.error("Invalid interval: {}", interval);
                return null;
        }

        if (timeSeries == null || !timeSeries.fields().hasNext()) {
            logger.error("Time series data is null or empty for ticker: {}, interval: {}", ticker, interval);
            return null;
        }
        logger.info("Time series data: {}", timeSeries.toString());

        Double percentageChange = calculatePercentageChange(timeSeries, timeSeriesField, numEntries);

        if (percentageChange == null) {
            logger.error("Percentage change calculation failed for ticker: {}, interval: {}", ticker, interval);
        } else {
            logger.info("Percentage change for ticker: {}, interval: {} is: {}", ticker, interval, percentageChange);
        }

        return percentageChange;
    }

    private Double calculatePercentageChange(JsonNode timeSeries, String timeSeriesField, int numEntries) {
        logger.info("Starting percentage change calculation. Required entries: {}", numEntries);

        if (timeSeries == null || !timeSeries.has(timeSeriesField)) {
            logger.error("Time series data is null or empty. Cannot perform calculation.");
            return null;
        }

        JsonNode timeSeriesData = timeSeries.get(timeSeriesField);
        Iterator<Map.Entry<String, JsonNode>> iterator = timeSeriesData.fields();

        List<Double> prices = new ArrayList<>();
        int count = 0;

        while (iterator.hasNext() && count < numEntries) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            double closePrice = entry.getValue().get("4. close").asDouble();
            prices.add(closePrice);
            logger.info("Extracted close price: {} at timestamp: {}", closePrice, entry.getKey());
            count++;
        }

        if (prices.size() < numEntries) {
            logger.error("Not enough prices extracted to calculate percentage change. Needed: {}, Extracted: {}", numEntries, prices.size());
            return null;
        }

        double mostRecentPrice = prices.get(0);
        double olderPrice = prices.get(prices.size() - 1);

        logger.info("Most recent price: {}, Older price: {}", mostRecentPrice, olderPrice);

        double percentageChange = ((mostRecentPrice - olderPrice) / olderPrice) * 100.0;
        logger.info("Calculated percentage change: {}%", percentageChange);

        return percentageChange;
    }





}
