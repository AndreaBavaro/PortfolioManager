package com.example.backend.service;

import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.model.Account;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceImpl implements AccountService{
    AccountRepository accountRepository;
    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account viewAccount(String nameCode) {
        return accountRepository.findByNameCode(nameCode);
    }
}
