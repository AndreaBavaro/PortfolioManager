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
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByNameCode(String nameCode);
    List<Account> findByPortfolioId(long portfolioId);

    @Query(value = "SELECT s.* FROM account_watchlist aw JOIN stocks s ON aw.ticker = s.ticker WHERE aw.name_code = :nameCode", nativeQuery = true)
    List<Stock> viewWatchlistByAccount(@Param("nameCode") String nameCode);

    @Query("SELECT i FROM Investment i WHERE i.account.nameCode = :nameCode")
    List<Investment> findAllInvestmentsByAccount(@Param("nameCode") String nameCode);

    @Query("SELECT t FROM Transaction t WHERE t.account.nameCode = :nameCode")
    List<Transaction> findAllTransactionsByAccount(@Param("nameCode") String nameCode);

}

