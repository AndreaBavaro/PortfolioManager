package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    @Entity
    @Table(name = "watchlist")
    public class Watchlist {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "account_id")
        private String accountId;

        @Column(name = "stock_ticker")
        private String stockTicker;

    }

