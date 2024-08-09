package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Exchange {
    private String marketIdentifierCode;

    public void setMarketIdentifierCode(String marketIdentifierCode) {
        this.marketIdentifierCode = marketIdentifierCode;
    }

    @Id
    public String getMarketIdentifierCode() {
        return marketIdentifierCode;
    }
}
