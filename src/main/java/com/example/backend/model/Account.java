package com.example.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Account {
    @Id
    private String nameCode;
    private String name;
    private long deposit;

    @ManyToMany
    @JoinTable(
            name = "stock_per_account",
            joinColumns = @JoinColumn(name = "account"),
            inverseJoinColumns = @JoinColumn(name = "stock")
    )
    Set<Stock> allStocks;

    public Account(String nameCode, String name, long deposit) {
        this.nameCode = nameCode;
        this.name = name;
        this.deposit = deposit;
    }

    public Account() {

    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }
}
