package com.jt.securetrading.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.securetrading.models.wallets.CryptoWallet;
import com.jt.securetrading.models.wallets.FlatWallet;
import com.jt.securetrading.repositories.CryptoWalletRepository;
import com.jt.securetrading.repositories.FlatWalletRepository;
import com.jt.securetrading.services.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/crypto")
public class CryptoWalletController {

    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;

    @Autowired
    private FlatWalletRepository flatWalletRepository;

    @Autowired
    private CryptoWalletService cryptoWalletService;

    @Value("${coingecko.api.key}")
    private String API_KEY;

    // Fetch cryptocurrency details
    @GetMapping("/coin-info/{coinId}")
    public ResponseEntity<String> getCoinInfo(@PathVariable String coinId) throws IOException, InterruptedException {
        return cryptoWalletService.getCoinInfo(coinId);
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyCrypto(@RequestParam String coinId,
                                            @RequestParam BigDecimal numCoins,
                                            @AuthenticationPrincipal UserDetails userDetails) throws IOException, InterruptedException {
        String username = userDetails.getUsername();
        return cryptoWalletService.buyCrypto(coinId, numCoins, username);
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellCrypto(@RequestParam String coinId,
                                             @RequestParam BigDecimal numCoinsToSell,
                                             @AuthenticationPrincipal UserDetails userDetails) throws IOException, InterruptedException {
        String username = userDetails.getUsername(); // Fetch ownerUsername from UserDetails
        return cryptoWalletService.sellCrypto(coinId, numCoinsToSell, username);

    }
}