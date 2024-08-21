package com.example.backend.service;

import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.dataaccess.InvestmentRepository;
import com.example.backend.model.Investment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestmentServiceImpl implements  InvestmentService{

    InvestmentRepository investmentRepository;

    @Override
    public List<Investment> getAllInvestments() {
        return investmentRepository.findAll();
    }

    @Override
    public Investment findInvestmentByTicker(String ticker) {
        // TODO: throw exception if not found
        return investmentRepository.findInvestmentByTicker(ticker).orElseThrow();
    }

    @Autowired
    public void setInvestmentRepository(InvestmentRepository investmentRepository){
        this.investmentRepository = investmentRepository;
    }
}
