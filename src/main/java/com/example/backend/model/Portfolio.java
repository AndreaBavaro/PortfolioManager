package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Portfolio in the system.
 */
@Getter
@Setter
@Entity
@Table(name = "portfolios") // Ensure table name matches database
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private float balance;

    @Column(name = "total_cash")
    private float totalCash;

    @Column(name = "total_investments")
    private float totalInvestments;

    @JsonIgnore
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public Portfolio(String name, float balance) {
        this.name = name;
        this.balance = balance;
        this.totalCash = balance;  // Assuming initial balance is all cash
        this.totalInvestments = 0f;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setPortfolio(this);
    }

    public void withdraw(float amount) {
        this.totalCash -= amount;
        this.balance = this.totalCash + this.totalInvestments;
    }

    public void deposit(float amount) {
        this.totalCash += amount;
        this.balance = this.totalCash + this.totalInvestments;
    }

    public float getBalance() {
        return balance;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
