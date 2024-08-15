package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    private Iterable<Stock> watchList;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "stock_per_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    private Set<Stock> allStocks;

    public Account(String nameCode, String accountType, float balance) {  // Updated parameter type
        this.nameCode = nameCode;
        this.accountType = accountType;
        this.balance = balance;
        this.totalCash = balance; // Assuming initial balance is all cash
        this.totalInvestments = 0f;
    }

    public Account() {
    }
    public void withdraw(float amount){
        this.balance -= amount;
    }
    public void deposit(float amount){
        this.balance += amount;
    }
}
