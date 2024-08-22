package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private float totalInvestments;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(
            name = "account_watchlist",
            joinColumns = @JoinColumn(name = "name_code", referencedColumnName = "name_code")
    )
    @Column(name = "ticker")
    private Set<String> watchList = new HashSet<>();


    public Account(String nameCode, String accountType, float balance, Portfolio portfolio) {
        this.nameCode = nameCode;
        this.accountType = accountType;
        this.totalCash = balance;
        this.balance = balance;
        this.totalInvestments = 0;
        this.portfolio = portfolio;
        this.watchList = new HashSet<>();
    }

    public Account() {
        this.watchList = new HashSet<>();
    }

    public void withdraw(float amount) {
        this.totalCash -= amount;
        this.balance = this.totalCash + this.totalInvestments;
    }

    public void deposit(float amount) {
        this.totalCash += amount;
        this.balance = this.totalCash + this.totalInvestments;
    }

    public void addToTotalInvestments(float investmentCost) {
        this.totalInvestments += investmentCost;
        this.balance = this.totalCash + this.totalInvestments;
    }

    public void removeFromTotalInvestments(float investmentCost) {
        this.totalInvestments -= investmentCost;
        this.balance = this.totalCash + this.totalInvestments;
    }

    public void addToWatchList(Stock stock) {
        this.watchList.add(stock.getTicker());
    }

    public void removeFromWatchList(Stock stock) {
        this.watchList.remove(stock.getTicker());
    }
}
