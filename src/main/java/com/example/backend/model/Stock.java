package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Stock {

    @Id
    private String ticker;

    private String name;
    private Long price;
    private Long high;
    private Long low;
    private Long vol;

    @ManyToOne
    private Exchange exchange;

    public Stock(String ticker, String name, Long price, Long high, Long low, Long vol, Exchange exchange) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.high = high;
        this.low = low;
        this.vol = vol;
        this.exchange = exchange;
    }

    public Stock() {

    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getHigh() {
        return high;
    }

    public void setHigh(Long high) {
        this.high = high;
    }

    public Long getLow() {
        return low;
    }

    public void setLow(Long low) {
        this.low = low;
    }

    public Long getVol() {
        return vol;
    }

    public void setVol(Long vol) {
        this.vol = vol;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}
