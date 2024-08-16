package com.example.backend.model;

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
public class Account {
    @Id
    private String nameCode;
    private String accountType;
    private float balance;            // Changed from long to float
    private float totalInvestments;   // Changed from long to float
    private float totalCash;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "watchlist", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "ticker")
    private Set<String> watchList = new HashSet<>();

    public Account(String nameCode, String accountType, float balance) {
        this.nameCode = nameCode;
        this.accountType = accountType;
        this.balance = balance;
        this.totalCash = balance; // Assuming initial balance is all cash
        this.totalInvestments = 0f;
        this.watchList = new HashSet<>();  // Initialize watchList
    }

    public Account() {
        this.watchList = new HashSet<>();  // Initialize watchList
    }

    public void withdraw(float amount){
        this.balance -= amount;
    }

    public void deposit(float amount){
        this.balance += amount;
    }

    public void addToWatchList(String ticker){
        this.watchList.add(ticker);
    }

    public void removeFromWatchList(String ticker){
        this.watchList.remove(ticker);
    }
}
