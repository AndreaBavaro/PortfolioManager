package com.example.backend.dataaccess;

import com.example.backend.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT i FROM Investment i WHERE i.ticker = :ticker AND i.account.nameCode = :nameCode")
    Optional<Investment> findInvestmentByTickerAndNameCode(@Param("ticker") String ticker, @Param("nameCode") String nameCode);

}
