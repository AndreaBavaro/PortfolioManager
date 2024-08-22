package com.example.backend.service;

import com.example.backend.ResourceNotFoundException;
import com.example.backend.dataaccess.AccountRepository;
import com.example.backend.dataaccess.InvestmentRepository;
import com.example.backend.dataaccess.TransactionRepository;
import com.example.backend.model.Account;
import com.example.backend.model.Investment;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements  TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    InvestmentRepository investmentRepository;

    @Autowired
    AccountRepository accountRepository;

    private void validateTransaction(Transaction transaction, boolean isBuy) {
        Account account = transaction.getAccount();
        Stock stock  = transaction.getStock();
        float transactionCost = transaction.getAmount() * stock.getPrice();

        // buying
        if (isBuy) {
            float availableCash = account.getTotalCash();
            // check if enough cash in account
            if (transactionCost > availableCash) {
                throw new IllegalArgumentException("Insufficient Funds for Buying");
            }
        } else {
            // selling
            String ticker = stock.getTicker();
            Optional<Investment> investment = investmentRepository.findInvestmentByTickerAndNameCode(ticker, account.getNameCode());
            if (!investment.isPresent()) {
                throw new ResourceNotFoundException("Cannot Find Investment with Ticker '" + ticker + "'"); // check if enough quantity in account
            }

            if (transaction.getAmount() > investment.get().getQuantity()) {
                throw new IllegalArgumentException("Insufficient Quantity for Selling");
            }
        }
    }



    @Override
    public Transaction buyTransaction(String type, int amount, Account account, Stock stock) {

        //create new transaction entity
        Transaction transaction =  new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setAccount(account);
        transaction.setStock(stock);

        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        transaction.setTransactionTime(currentTimestamp);

        // check if transaction is valid
        validateTransaction(transaction, true);

        // find investment with the given ticker in account
        Optional<Investment> investment = investmentRepository.findInvestmentByTickerAndNameCode(stock.getTicker(), account.getNameCode());

        // update investment if it exists, otherwise create new investment
        if (investment.isPresent()) {
            int currentQuantity = investment.get().getQuantity();
            investment.get().setQuantity(currentQuantity + transaction.getAmount());
            investment.get().addTransaction(transaction);
        } else {
            createNewInvestment(transaction, stock);
        }

        // update balance and investments in account
        float transactionCost = transaction.getAmount() * stock.getPrice();
        account.withdraw(transactionCost);
        account.addToTotalInvestments(transactionCost);

        // save transaction and return
        return transactionRepository.save(transaction);
    }

    private void createNewInvestment(Transaction transaction, Stock stock) {
        Investment newInvestment = new Investment();
        newInvestment.setQuantity(transaction.getAmount());
        newInvestment.setTicker(stock.getTicker());
        newInvestment.setAccount(transaction.getAccount());
        newInvestment.setStock(stock);
        newInvestment.addTransaction(transaction);
        investmentRepository.save(newInvestment);
    }

    @Override
    public Transaction sellTransaction(String type, int amount, Account account, Stock stock) {

        //create new transaction entity
        Transaction transaction =  new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setAccount(account);
        transaction.setStock(stock);

        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        transaction.setTransactionTime(currentTimestamp);

        // check if transaction is valid
        validateTransaction(transaction, false);

        // update investment, and delete investment in quantity is 0
        Optional<Investment> investment = investmentRepository.findInvestmentByTickerAndNameCode(stock.getTicker(), account.getNameCode());
        investment.get().addTransaction(transaction);

        int updatedQuantity = investment.get().getQuantity() - transaction.getAmount();
        if (updatedQuantity > 0) {
            investment.get().setQuantity(updatedQuantity);
        } else {
            long id = investment.get().getId();
            investmentRepository.deleteById(id);
        }

        // update balance and investments in account
        float transactionCost = transaction.getAmount() * stock.getPrice();
        account.deposit(transactionCost);
        account.removeFromTotalInvestments(transactionCost);

        // save transaction and return
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
