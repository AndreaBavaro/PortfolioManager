package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "name_code", nullable = false, unique = true)
    private String nameCode;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "balance", nullable = false)
    private float balance;

    @Column(name = "total_investments", nullable = false)
    private long totalInvestments; // Changed to long

    @Column(name = "total_cash", nullable = false)
    private float totalCash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnore
    private Portfolio portfolio;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "account_watchlist",
            joinColumns = @JoinColumn(name = "name_code"),
            inverseJoinColumns = @JoinColumn(name = "ticker")
    )
    private Set<Stock> watchList = new HashSet<>();

    public Account(String nameCode, String accountType, float balance, Portfolio portfolio) {
        this.nameCode = nameCode;
        this.accountType = accountType;
        this.balance = balance;
        this.totalCash = balance;
        this.totalInvestments = 0;
        this.portfolio = portfolio;
        this.watchList = new HashSet<>();
    }

    public Account() {
        this.watchList = new HashSet<>();
    }

    public void withdraw(float amount) {
        this.balance -= amount;
    }

    public void deposit(float amount) {
        this.balance += amount;
    }

    public void addToWatchList(Stock stock) {
        this.watchList.add(stock);
    }

    public void removeFromWatchList(Stock stock) {
        this.watchList.remove(stock);
    }
}
