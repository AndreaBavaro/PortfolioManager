package com.example.backend.service;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.dataaccess.StockRepository;
import com.example.backend.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockServiceImpl implements StockService{
    StockRepository stockRepository;
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

    @Autowired
    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
}
