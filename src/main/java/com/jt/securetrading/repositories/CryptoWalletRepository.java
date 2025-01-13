package com.jt.securetrading.repositories;

import com.jt.securetrading.models.Bank;
import com.jt.securetrading.models.wallets.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet,Long> {
    Optional<CryptoWallet> findByOwnerUsername(String username);

    Optional<CryptoWallet> findByOwnerUsernameAndCoinName(String username, String coinName);

    List<CryptoWallet> findAllByOwnerUsername(String ownerUsername);
}
