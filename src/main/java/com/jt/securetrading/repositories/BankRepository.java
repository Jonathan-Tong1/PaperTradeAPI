package com.jt.securetrading.repositories;

import com.jt.securetrading.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByOwnerUsername(String username);
}
