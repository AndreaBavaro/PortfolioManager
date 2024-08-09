package com.example.backend.service;

import com.example.backend.model.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    public Iterable<Account> getAllAccounts();
    public Account viewAccount(String nameCode);
}
