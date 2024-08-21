package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "account_watchlist")
public class AccountWatchlist {

    @EmbeddedId
    private AccountWatchlistId id;

    public AccountWatchlist() {}

    public AccountWatchlist(AccountWatchlistId id) {
        this.id = id;
    }

    public AccountWatchlistId getId() {
        return id;
    }

    public void setId(AccountWatchlistId id) {
        this.id = id;
    }

    @Embeddable
    public static class AccountWatchlistId implements Serializable {

        @Column(name = "name_code", length = 255)
        private String nameCode;

        @Column(name = "ticker")
        private String ticker;

        public AccountWatchlistId() {}

        public AccountWatchlistId(String nameCode, String ticker) {
            this.nameCode = nameCode;
            this.ticker = ticker;
        }

        public String getNameCode() {
            return nameCode;
        }

        public void setNameCode(String nameCode) {
            this.nameCode = nameCode;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccountWatchlistId that = (AccountWatchlistId) o;
            return Objects.equals(nameCode, that.nameCode) && Objects.equals(ticker, that.ticker);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nameCode, ticker);
        }
    }
}
