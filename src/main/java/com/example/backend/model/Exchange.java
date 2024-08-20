package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exchanges") // Ensure table name matches database
public class Exchange {

    @Id
    @Column(name = "market_identifier_code", nullable = false, unique = true)
    // Prevents serialization of the exchange field// Column definition
    private String marketIdentifierCode;

    // Default constructor
    public Exchange() {
    }
}
