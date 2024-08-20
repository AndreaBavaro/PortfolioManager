package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "stocks") // Use plural form for table names
public class Stock {

    @Id
    @Column(name = "ticker", nullable = false) // Primary key
    private String ticker;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "high")
    private Long high;

    @Column(name = "low")
    private Long low;

    @Column(name = "vol")
    private Long vol;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "exchange_market_identifier_code")
    @JsonIgnore
    private Exchange exchange;

    // Default constructor
    public Stock() {
    }

    // Constructor with all fields except timestamp
    public Stock(String ticker, String name, Long price, Long high, Long low, Long vol, Exchange exchange) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.high = high;
        this.low = low;
        this.vol = vol;
        this.exchange = exchange;
    }

    // Constructor with all fields
    public Stock(String ticker, String name, Long price, Long high, Long low, Long vol, Exchange exchange, Timestamp timestamp) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.high = high;
        this.low = low;
        this.vol = vol;
        this.exchange = exchange;
        this.timestamp = timestamp;
    }
}
