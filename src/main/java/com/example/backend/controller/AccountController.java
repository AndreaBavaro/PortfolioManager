package com.example.backend.controller;

import com.example.backend.model.Account;
import com.example.backend.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    AccountService accountService;

    @GetMapping("/account")
    public Iterable<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{nameCode}")
    public Account viewAccount(@PathVariable String nameCode) {
        return accountService.viewAccount(nameCode);
    }

//    @PostMapping("/account")
//    public
}
