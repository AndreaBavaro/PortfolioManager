package com.example.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private float balance;          // Changed from long to float
    private float totalCash;        // Changed from long to float
    private float totalInvestments; // Changed from long to float

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public Portfolio(String name, float balance) {  // Updated parameter type
        this.name = name;
        this.balance = balance;
        this.totalCash = balance;  // Assuming initial balance is all cash
        this.totalInvestments = 0f;
    }
    public void addAccount(Account account){
        this.accounts.add(account);
    }
    public void withdraw(float amount){
        this.balance -= amount;
    }
    public void deposit(float amount){
        this.balance += amount;
    }

    public float getBalance() {
        return balance;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
