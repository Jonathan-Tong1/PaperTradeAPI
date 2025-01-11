package com.jt.securetrading.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.securetrading.models.wallets.FlatWallet;
import com.jt.securetrading.models.wallets.StockWallet;
import com.jt.securetrading.repositories.FlatWalletRepository;
import com.jt.securetrading.repositories.StockWalletRepository;
import com.jt.securetrading.services.StockTradeWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


@RestController
@RequestMapping("/api/stock-wallet/")
public class StockTradeWalletController {

    private static final String FINNHUB_API_BASE_URL = "https://finnhub.io/api/v1/";

    @Value("${finnhub.api.key}")
    private String API_KEY;

    @Value("{finnhub.secret.key}")
    private String API_SECRET;

    @Autowired
    private StockTradeWalletService stockTradeWalletService;

    @PostMapping("/buy-stock")
    public ResponseEntity<String> buyStock(@RequestParam String stockSymbol,
                                           @RequestParam BigDecimal numShares,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        return stockTradeWalletService.buyStock(stockSymbol, numShares, username);

    }

    @PostMapping("/sell-stock")
    public ResponseEntity<String> sellStock(@RequestParam String stockSymbol,
                                            @RequestParam BigDecimal numSharesToSell,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return stockTradeWalletService.sellStock(stockSymbol, numSharesToSell, username);

    }
}