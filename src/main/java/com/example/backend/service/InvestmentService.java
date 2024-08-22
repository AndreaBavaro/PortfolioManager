package com.example.backend.service;

import com.example.backend.model.Investment;

import java.util.List;
import java.util.Optional;

public interface InvestmentService {
    List<Investment> getAllInvestments();
    Investment findInvestmentByTicker(String ticker, String nameCode);
}
