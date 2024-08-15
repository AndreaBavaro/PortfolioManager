package com.example.backend.dataaccess;

import com.example.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
    <Optional>Portfolio findById(long id);

    @Query("SELECT a FROM Account a WHERE a.portfolio.id = :portfolioId")
    List<Account> findAllAccountsByPortfolio(@Param("portfolioId") Long portfolioId);



}