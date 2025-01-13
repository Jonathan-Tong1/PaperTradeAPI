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

        return cryptoWalletService.getCryptoAssetsSummary(username);

    }

    @GetMapping("/stockwallet")
    public ResponseEntity<Map<String, Object>> getTradingWalletSummary(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User details are missing."));
        }

        String username = userDetails.getUsername();
        return stockTradeWalletService.getTradingWalletSummary(username);


    }

}
