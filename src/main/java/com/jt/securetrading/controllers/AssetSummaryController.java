package com.jt.securetrading.controllers;

import com.jt.securetrading.models.wallets.CryptoWallet;
import com.jt.securetrading.services.CryptoWalletService;
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


}
