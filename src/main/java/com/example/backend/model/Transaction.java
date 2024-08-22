package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "transactions") // Use plural form for table names
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionid") // Primary key
    private long transactionID;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private int amount;

    @Column(name = "transaction_time")
    private Timestamp transactionTime;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    // Default constructor
    public Transaction() {
    }

    // Constructor with necessary fields
    public Transaction(String type, int amount, Timestamp transactionTime, Account account, Stock stock) {
        this.type = type;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.account = account;
        this.stock = stock;
    }
}
