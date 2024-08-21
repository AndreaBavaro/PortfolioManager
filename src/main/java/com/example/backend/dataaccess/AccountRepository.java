package com.example.backend.dataaccess;

import com.example.backend.model.Account;
import com.example.backend.model.Investment;
import com.example.backend.model.Stock;
import com.example.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByNameCode(String nameCode);

    List<Account> findByPortfolioId(long portfolioId);

    @Query(value = "SELECT s FROM Stock s " +
            "JOIN Account a ON a.nameCode = :nameCode " +
            "JOIN a.watchList w ON w = s.ticker")
    List<Stock> viewWatchlistByAccount(@Param("nameCode") String nameCode);

    @Query("SELECT i FROM Investment i WHERE i.account.nameCode = :nameCode")
    List<Investment> findAllInvestmentsByAccount(@Param("nameCode") String nameCode);

    @Query("SELECT t FROM Transaction t WHERE t.account.nameCode = :nameCode")
    List<Transaction> findAllTransactionsByAccount(@Param("nameCode") String nameCode);
}
