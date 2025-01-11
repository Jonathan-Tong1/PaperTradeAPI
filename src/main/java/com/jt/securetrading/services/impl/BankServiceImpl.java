package com.jt.securetrading.services.impl;

import com.jt.securetrading.models.Bank;
import com.jt.securetrading.repositories.BankRepository;
import com.jt.securetrading.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public Optional<Bank> findByOwner(String ownerUsername) {
        return Optional.empty();
    }
}
