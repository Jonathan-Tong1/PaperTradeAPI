package com.jt.securetrading.controllers;


import com.jt.securetrading.models.Bank;
import com.jt.securetrading.models.wallets.FlatWallet;
import com.jt.securetrading.repositories.BankRepository;
import com.jt.securetrading.repositories.FlatWalletRepository;
import com.jt.securetrading.services.FlatWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/flatwallet")
public class FlatWalletController {

    @Autowired
    private FlatWalletRepository flatWalletRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private FlatWalletService flatWalletService;

    // Withdraw money from flat wallet and deposit to bank
    @PostMapping("withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestParam BigDecimal amount,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return flatWalletService.withdrawFromBank(username, amount);
    }

    // Deposit money from bank to flat wallet
    @PostMapping("deposit")
    public ResponseEntity<String> depositMoney(@RequestParam BigDecimal amount,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return flatWalletService.depositToBank(username, amount);
    }
}