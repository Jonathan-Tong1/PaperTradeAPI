package com.jt.securetrading.controllers;

import com.jt.securetrading.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankRepository bankRepository;

    @GetMapping("balance")
    public ResponseEntity<BigDecimal> getBalance(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        // Fetch the bank details for the authenticated user
        return bankRepository.findByOwnerUsername(username)
                .map(bank -> ResponseEntity.ok(bank.getBalance())) // Map the balance field to the response
                .orElse(ResponseEntity.status(404).body(null)); // Return 404 if no bank account is found
    }
}