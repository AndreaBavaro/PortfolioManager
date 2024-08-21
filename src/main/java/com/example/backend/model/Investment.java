package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an Investment in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investments") // Ensure table name matches database
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "quantity")
    private int quantity; // Amount of shares in a stock

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "investment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    // Constructor with parameters for convenience
    public Investment(String currency, String ticker, int quantity, Account account, Stock stock) {
        this.currency = currency;
        this.ticker = ticker;
        this.quantity = quantity;
        this.account = account;
        this.stock = stock;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setInvestment(this);
    }
}
