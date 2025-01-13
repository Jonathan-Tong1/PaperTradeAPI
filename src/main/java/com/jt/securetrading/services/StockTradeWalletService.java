package com.jt.securetrading.services;

import com.jt.securetrading.models.wallets.StockWallet;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface StockTradeWalletService {


    ResponseEntity<Map<String, Object>> getTradingWalletSummary(String username);

    List<StockWallet> getStockAssetsByUser(String username);

    public ResponseEntity<String> buyStock(String stockSymbol, BigDecimal numShares, String ownerUsername);

    public ResponseEntity<String> sellStock(String stockId, BigDecimal numShares, String ownerUsername);

}
