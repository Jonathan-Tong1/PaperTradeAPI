package com.jt.securetrading.models.wallets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class CryptoWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coin_ticker_symbol")
    private String coinTickerSymbol;

    @Column(name = "coin_name")
    private String coinName;

    @Column(name = "number_of_coins")
    private BigDecimal numOfCoins = BigDecimal.ZERO;

    @Column(name="owner")
    private String ownerUsername;


    public CryptoWallet() {
    }

    public CryptoWallet(String coinTickerSymbol, String coinName, BigDecimal numOfCoins, String ownerUsername) {
        this.coinTickerSymbol = coinTickerSymbol;
        this.coinName = coinName;
        this.numOfCoins = numOfCoins;
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String walletOwner) {
        this.ownerUsername = walletOwner;
    }

    public String getCoinTickerSymbol() {
        return coinTickerSymbol;
    }

    public void setCoinTickerSymbol(String coinTickerSymbol) {
        this.coinTickerSymbol = coinTickerSymbol;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public BigDecimal getNumOfCoins() {
        return numOfCoins;
    }

    public void setNumOfCoins(BigDecimal amount) {
        this.numOfCoins = amount;
    }

    @Override
    public String toString() {
        return "CryptoWallet{" +
                "id=" + id +
                ", coinTickerSymbol='" + coinTickerSymbol + '\'' +
                ", coinName='" + coinName + '\'' +
                ", amount=" + numOfCoins +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CryptoWallet that = (CryptoWallet) o;
        return Objects.equals(id, that.id) && Objects.equals(coinTickerSymbol, that.coinTickerSymbol) && Objects.equals(coinName, that.coinName) && Objects.equals(numOfCoins, that.numOfCoins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coinTickerSymbol, coinName, numOfCoins);
    }
}
