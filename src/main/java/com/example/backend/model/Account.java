package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Account {
    @Id
    private String nameCode;

    @ManyToMany
    private Stock stock;

}
