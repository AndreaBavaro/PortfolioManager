package com.example.backend.service;

import com.example.backend.dataaccess.InvestmentRepository;
import com.example.backend.dataaccess.TransactionRepository;
import com.example.backend.model.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InvestmentServiceImpl implements  InvestmentService{

    InvestmentRepository investmentRepository;

    @Override
    public Investment createInvestment(Investment investment) {
        return null;
    }

    @Override
    public Investment deleteInvestment(Investment investment) {
        return null;
    }

    @Override
    public Investment updateInvestment(Investment investment) {
        return null;
    }

    @Override
    public List<Investment> showAllInvestments(long accountID) {
        return List.of();
    }
    @Autowired
    public void setInvestmentRepository(InvestmentRepository investmentRepository){
        this.investmentRepository = investmentRepository;
    }
}
