package com.example.backend.dataaccess;

import com.example.backend.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    // Method to find a Stock by its ticker without returning Optional
    Stock findByTicker(String ticker);

    // Method to find all Stock objects by a set of tickers
    List<Stock> findAllByTickerIn(Set<Stock> tickers);
}
