package com.example.backend.dataaccess;

import com.example.backend.model.Account;
import com.example.backend.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByNameCode(String nameCode);

    @Query("SELECT stock from stock_per_account s WHERE s.account = :nameCode")
    List<Stock> findAllStocksByAccount(@Param("nameCode") String nameCode);
}
