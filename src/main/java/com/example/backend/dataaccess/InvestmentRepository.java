package com.example.backend.dataaccess;

import com.example.backend.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT * FROM investment WHERE ticker = :ticker")
    Optional<Investment> findInvestmentByTicker(@Param("ticker") String ticker);
}
