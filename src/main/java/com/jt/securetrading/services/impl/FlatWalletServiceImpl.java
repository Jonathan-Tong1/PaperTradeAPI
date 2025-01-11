package com.jt.securetrading.services.impl;

import com.jt.securetrading.models.Bank;
import com.jt.securetrading.models.wallets.FlatWallet;
import com.jt.securetrading.repositories.BankRepository;
import com.jt.securetrading.repositories.FlatWalletRepository;
import com.jt.securetrading.services.FlatWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FlatWalletServiceImpl implements FlatWalletService {

    @Autowired
    private FlatWalletRepository flatWalletRepository;

    @Autowired
    private BankRepository bankRepository;

    @Override
    public ResponseEntity<String> withdrawFromBank(String ownerUsername, BigDecimal amount) {
        FlatWallet wallet = flatWalletRepository.findByOwnerUsername(ownerUsername)
                .orElse(null);

        if (wallet == null) {
            return ResponseEntity.status(404).body("Wallet not found for user: " + ownerUsername);
        }

        // Step 2: Check wallet balance
        BigDecimal currentBalance = wallet.getBalance();
        if (currentBalance.compareTo(amount) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance in wallet!");
        }

        // Step 3: Fetch bank account associated with the user
        Bank bank = bankRepository.findByOwnerUsername(ownerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found for user: " + ownerUsername));

        // Step 4: Deduct from wallet and add to bank
        wallet.setBalance(currentBalance.subtract(amount));
        bank.setBalance(bank.getBalance().add(amount));

        // Step 5: Save updated wallet and bank
        flatWalletRepository.save(wallet);
        bankRepository.save(bank);

        // Step 6: Return success response
        return ResponseEntity.ok("Withdrawal successful! New wallet balance: " + wallet.getBalance() +
                ". New bank balance: " + bank.getBalance());
    }

    @Override
    public ResponseEntity<String> depositToBank(String ownerUsername, BigDecimal amount) {
        FlatWallet wallet = flatWalletRepository.findByOwnerUsername(ownerUsername)
                .orElse(null);

        if (wallet == null) {
            return ResponseEntity.status(404).body("Wallet not found for user: " + ownerUsername);
        }

        // Step 2: Fetch bank account associated with the user
        Bank bank = bankRepository.findByOwnerUsername(ownerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found for user: " + ownerUsername));

        // Step 3: Check bank balance
        BigDecimal currentBankBalance = bank.getBalance();
        if (currentBankBalance.compareTo(amount) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance in bank!");
        }

        // Step 4: Deduct from bank and add to wallet
        bank.setBalance(currentBankBalance.subtract(amount)); // Subtract from bank
        wallet.setBalance(wallet.getBalance().add(amount));   // Add to wallet

        // Step 5: Save updated wallet and bank
        flatWalletRepository.save(wallet);
        bankRepository.save(bank);

        // Step 6: Return success response
        return ResponseEntity.ok("Deposit successful! New wallet balance: " + wallet.getBalance() +
                ". New bank balance: " + bank.getBalance());
    }
}
