package com.jt.securetrading.repositories;

import com.jt.securetrading.models.wallets.CryptoWallet;
import com.jt.securetrading.models.wallets.StockWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockWalletRepository extends JpaRepository<StockWallet,Long> {

    Optional<StockWallet> findByOwnerUsername(String username);

    Optional<StockWallet> findByOwnerUsernameAndStockSymbol(String ownerUsername, String stockSymbol);

    List<StockWallet> findAllByOwnerUsername(String username);
}
