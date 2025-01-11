package com.jt.securetrading.services;

import com.jt.securetrading.models.Bank;

import java.util.Optional;

public interface BankService {
    Optional<Bank> findByOwner(String ownerUsername);
}
