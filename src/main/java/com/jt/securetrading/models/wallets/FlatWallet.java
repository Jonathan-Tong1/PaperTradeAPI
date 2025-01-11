package com.jt.securetrading.models.wallets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class FlatWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerUsername;

    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlatWallet that = (FlatWallet) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerUsername, that.ownerUsername) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUsername, balance);
    }

    public FlatWallet() {
    }

    public FlatWallet(String ownerUsername, BigDecimal balance) {
        this.ownerUsername = ownerUsername;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "FlatWallet{" +
                "id=" + id +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
