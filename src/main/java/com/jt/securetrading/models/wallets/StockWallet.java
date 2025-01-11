package com.jt.securetrading.models.wallets;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class StockWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockSymbol;

    private BigDecimal stockPrice;

    private BigDecimal numShares;

    @Column(nullable = false)
    private String ownerUsername;

    // Default constructor for JPA
    public StockWallet() {
    }

    // Constructor to initialize fields
    public StockWallet(String stockSymbol, BigDecimal stockPrice, BigDecimal numShares, String ownerUsername) {
        this.stockSymbol = stockSymbol;
        this.stockPrice = stockPrice;
        this.numShares = numShares;
        this.ownerUsername = ownerUsername;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    public BigDecimal getNumShares() {
        return numShares;
    }

    public void setNumShares(BigDecimal numShares) {
        this.numShares = numShares;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    @Override
    public String toString() {
        return "StockWallet{" +
                "id=" + id +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", stockPrice=" + stockPrice +
                ", numShares=" + numShares +
                ", ownerUsername='" + ownerUsername + '\'' +
                '}';
    }
}