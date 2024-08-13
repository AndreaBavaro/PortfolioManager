package com.example.backend.service;

import com.example.backend.model.Account;
import com.example.backend.model.Investment;

import java.util.List;

public interface InvestmentService {
    public Investment createInvestment(Investment investment);
    public Investment deleteInvestment(Investment investment);
    public Investment updateInvestment(Investment investment);
    public List<Investment> showAllInvestments(long accountID);


}
