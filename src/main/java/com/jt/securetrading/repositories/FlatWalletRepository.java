package com.jt.securetrading.repositories;

import com.jt.securetrading.models.wallets.FlatWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FlatWalletRepository extends JpaRepository<FlatWallet, Long> {
    Optional<FlatWallet> findByOwnerUsername(String ownerUsername);
}