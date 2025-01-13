package com.jt.securetrading.services;

import com.jt.securetrading.models.wallets.CryptoWallet;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface CryptoWalletService {
    ResponseEntity<String> getCoinInfo(String coin);

    ResponseEntity<String> buyCrypto(String coinId, BigDecimal numCoins, String ownerUsername) throws IOException, InterruptedException;

    ResponseEntity<String> sellCrypto(String coinId, BigDecimal numCoins, String ownerUsername) throws IOException, InterruptedException;

    List<CryptoWallet> getCryptoAssetsByUser(String username);
}
