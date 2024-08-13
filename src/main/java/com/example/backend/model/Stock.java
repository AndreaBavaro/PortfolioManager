package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
@Setter
@Getter
@Entity
public class Stock {

    @Id
    private String ticker;
    private String name;
    private Long price;
    private Long high;
    private Long low;
    private Long vol;

    private Timestamp timestamp;

    @ManyToOne
    private Exchange exchange;


    @ManyToMany(mappedBy = "allStocks")
    Set<Account> allAccounts;


    public Stock(String ticker, String name, Long price, Long high, Long low, Long vol, Exchange exchange, Timestamp timestamp) {

    }

    public Stock(String ticker, String name, Long price, Long high, Long low, Long vol, Exchange exchange) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.high = high;
        this.low = low;
        this.vol = vol;
        this.exchange = exchange;
        this.timestamp = timestamp;
    }

    public Stock() {

    }
}
