package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.ToOne;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private String currency;
    private String ticker;
    private int quantity;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "investment")
    private List<Transaction> transactions;

    @OneToOne
    private Stock stock;
}
