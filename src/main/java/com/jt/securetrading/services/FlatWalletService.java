package com.jt.securetrading.services;

import com.jt.securetrading.models.wallets.FlatWallet;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface FlatWalletService {

    ResponseEntity<String> withdrawFromBank(String ownerUsername, BigDecimal amount);

    ResponseEntity<String>  depositToBank(String ownerUsername, BigDecimal amount);
}
