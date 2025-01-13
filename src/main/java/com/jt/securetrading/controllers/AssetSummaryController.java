package com.jt.securetrading.controllers;

import com.jt.securetrading.models.wallets.CryptoWallet;
import com.jt.securetrading.models.wallets.StockWallet;
import com.jt.securetrading.services.CryptoWalletService;
import com.jt.securetrading.services.StockTradeWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
public class AssetSummaryController {

    @Autowired
    private CryptoWalletService cryptoWalletService;
    @Autowired
    private StockTradeWalletService stockTradeWalletService;

    @GetMapping("/cryptowallet")
    public ResponseEntity<Map<String, Object>> getCryptoAssetsSummary(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User details are missing."));
        }

        String username = userDetails.getUsername();

        try {
            // Fetch crypto wallet data for the user
            List<CryptoWallet> cryptoWallets = cryptoWalletService.getCryptoAssetsByUser(username);

            // Prepare response summary
            BigDecimal totalCoins = cryptoWallets.stream()
                    .map(CryptoWallet::getNumOfCoins)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<Map<String, Object>> assets = cryptoWallets.stream()
                    .map(wallet -> {
                        Map<String, Object> assetMap = new HashMap<>();
                        assetMap.put("coinName", wallet.getCoinName());
                        assetMap.put("ticker", wallet.getCoinTickerSymbol());
                        assetMap.put("amount", wallet.getNumOfCoins());
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = Map.of(
                    "username", username,
                    "totalCoins", totalCoins,
                    "assets", assets
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error (you can replace this with logging logic)
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while retrieving crypto asset summary."));
        }
    }

    @GetMapping("/stockwallet")
    public ResponseEntity<Map<String, Object>> getTradingWalletSummary(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User details are missing."));
        }

        String username = userDetails.getUsername();

        try {
            // Fetch stock wallet data for the user
            List<StockWallet> stockWallets = stockTradeWalletService.getStockAssetsByUser(username);

            // Calculate total number of stocks
            BigDecimal totalNumberOfStocks = stockWallets.stream()
                    .map(StockWallet::getNumShares)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Create list of assets
            List<Map<String, Object>> assets = stockWallets.stream()
                    .map(wallet -> {
                        Map<String, Object> assetMap = new HashMap<>();
                        assetMap.put("stockSymbol", wallet.getStockSymbol());
                        assetMap.put("numShares", wallet.getNumShares());
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            // Prepare response map
            Map<String, Object> response = Map.of(
                    "username", username,
                    "totalNumberOfStocks", totalNumberOfStocks,
                    "assets", assets
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while retrieving trading wallet summary."));
        }
    }

}
