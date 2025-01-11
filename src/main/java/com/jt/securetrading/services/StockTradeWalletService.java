package com.jt.securetrading.services;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface StockTradeWalletService {


    public ResponseEntity<String> buyStock(String stockSymbol, BigDecimal numShares, String ownerUsername);

    public ResponseEntity<String> sellStock(String stockId, BigDecimal numShares, String ownerUsername);

}
