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

    @Query("SELECT stock from stock_per_account s WHERE s.account = :nameCode")
    List<Stock> findAllStocksByAccount(@Param("nameCode") String nameCode);

    @Query("SELECT investment from Investments s WHERE s.account = :nameCode")
    List<Investment> findAllInvestmentsByAccount(@Param("nameCode") String nameCode);

    @Query("SELECT transaction from Transactions s WHERE s.account = :nameCode")
    List<Transaction>findAllTransactionsByAccount(@Param("nameCode") String id);
}
