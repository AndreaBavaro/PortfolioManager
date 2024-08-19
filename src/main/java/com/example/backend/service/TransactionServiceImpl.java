package com.example.backend.service;

import com.example.backend.dataaccess.InvestmentRepository;
import com.example.backend.dataaccess.TransactionRepository;
import com.example.backend.model.Account;
import com.example.backend.model.Investment;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionServiceImpl implements  TransactionService {
    TransactionRepository transactionRepository;
    InvestmentRepository investmentRepository;

    private boolean isValidTransaction(Transaction transaction, boolean isBuy) {
        Account account = transaction.getAccount();
        Stock stock  = transaction.getStock();
        float transactionCost = transaction.getAmount() * stock.getPrice();

        // buying
        if (isBuy) {
            float availableCash = account.getBalance();
            return transactionCost <= availableCash; // check if enough cash in account
        }

        // selling
        String ticker = stock.getTicker();
        Optional<Investment> investment = investmentRepository.findInvestmentByTicker(ticker);
        if (investment.isPresent()) {
            return transaction.getAmount() <= investment.get().getQuantity(); // check if enough quantity in account
        } else {
            // TODO: throw error
        }

        return false;
    }



    @Override
    public Transaction buyTransaction(Transaction transaction) {

        // check if transaction is valid
        if (!isValidTransaction(transaction, true)) {
            // TODO: throw exception
            return null;
        }

        Stock stock  = transaction.getStock();
        Optional<Investment> investment = investmentRepository.findInvestmentByTicker(stock.getTicker());

        // update investment if it exists, otherwise create new investment
        if (investment.isPresent()) {
            int currentQuantity = investment.get().getQuantity();
            investment.get().setQuantity(currentQuantity + transaction.getAmount());
        } else {
            handleNewInvestment(transaction, stock);
        }

        // update total cash in account
        Account account = transaction.getAccount();
        float transactionCost = transaction.getAmount() * stock.getPrice();
        account.deposit(transactionCost);

        return transactionRepository.save(transaction);
    }

    private void handleNewInvestment(Transaction transaction, Stock stock) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        Investment newInvestment = new Investment();
        newInvestment.setCurrency(""); // TODO: create currency field in stock
        newInvestment.setQuantity(transaction.getAmount());
        newInvestment.setTicker(stock.getTicker());
        newInvestment.setAccount(transaction.getAccount());
        newInvestment.setTransactions(transactions);
        newInvestment.setStock(stock);
        investmentRepository.save(newInvestment);

        // update list of investments in account entity
//        Account account = transaction.getAccount();
//        List<Investment> accountInvestments = account.getInvestments();
//        accountInvestments.add(newInvestment);
//        account.setInvestments(accountInvestments);
    }

    @Override
    public Transaction sellTransaction(Transaction transaction) {

        // check if transaction is valid
        if (!isValidTransaction(transaction, false)) {
            // TODO: throw exception
            return null;
        }

        // update investment quantity, and delete investment in quantity is 0
        Stock stock  = transaction.getStock();
        Optional<Investment> investment = investmentRepository.findInvestmentByTicker(stock.getTicker());
        int currentQuantity = investment.get().getQuantity();
        if (currentQuantity > 0) {
            investment.get().setQuantity(currentQuantity + transaction.getAmount());
        } else {
            investmentRepository.deleteById(investment.get().getId());
        }

        // update total cash in account
        Account account = transaction.getAccount();
        float transactionCost = transaction.getAmount() * stock.getPrice();
        account.withdraw(transactionCost);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setInvestmentRepository(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }
}
