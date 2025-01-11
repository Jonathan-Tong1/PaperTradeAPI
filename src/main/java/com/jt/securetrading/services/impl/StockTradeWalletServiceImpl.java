package com.jt.securetrading.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.securetrading.models.wallets.FlatWallet;
import com.jt.securetrading.models.wallets.StockWallet;
import com.jt.securetrading.repositories.FlatWalletRepository;
import com.jt.securetrading.repositories.StockWalletRepository;
import com.jt.securetrading.services.StockTradeWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class StockTradeWalletServiceImpl implements StockTradeWalletService {

    private static final String FINNHUB_API_BASE_URL = "https://finnhub.io/api/v1/";

    @Value("${finnhub.api.key}")
    private String API_KEY;

    @Value("{finnhub.secret.key}")
    private String API_SECRET;

    @Autowired
    private FlatWalletRepository flatWalletRepository;

    @Autowired
    private StockWalletRepository stockWalletRepository;

    @Override
    public ResponseEntity<String> buyStock(String stockSymbol, BigDecimal numShares, String ownerUsername) {
        try {

            // Step 1: Fetch stock price using your existing getCurrentStockPrice logic
            String apiUrl = FINNHUB_API_BASE_URL + "quote?symbol=" + stockSymbol + "&token=" + API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("X-Finnhub-Token", API_KEY)
                    .header("X-Finnhub-Secret", API_SECRET)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return ResponseEntity.status(response.statusCode()).body("Error fetching stock data for symbol '" + stockSymbol + "': " + response.body());
            }

            // Parse the stock price from the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());
            BigDecimal stockPrice = jsonResponse.get("c").decimalValue(); // Current price ("c" field)

            if (stockPrice.compareTo(BigDecimal.ZERO) == 0) {
                return ResponseEntity.badRequest().body("Invalid stock price for symbol: " + stockSymbol);
            }

            // Step 2: Calculate total price for the requested number of shares
            BigDecimal totalPrice = stockPrice.multiply(numShares);

            // Step 3: Fetch the user's FlatWallet
            FlatWallet flatWallet = flatWalletRepository.findByOwnerUsername(ownerUsername)
                    .orElseThrow(() -> new RuntimeException("Flat wallet not found for user: " + ownerUsername));

            // Step 4: Check if FlatWallet has sufficient balance
            if (flatWallet.getBalance().compareTo(totalPrice) < 0) {
                return ResponseEntity.badRequest().body("Insufficient balance in FlatWallet. Required: $" + totalPrice);
            }

            // Step 5: Fetch or create a new StockWallet for the given stock
            Optional<StockWallet> existingStockWallet = stockWalletRepository.findByOwnerUsernameAndStockSymbol(ownerUsername, stockSymbol);

            if (existingStockWallet.isPresent()) {
                // Increment the number of shares for the existing wallet
                StockWallet stockWallet = existingStockWallet.get();
                stockWallet.setNumShares(stockWallet.getNumShares().add(numShares));
                stockWalletRepository.save(stockWallet);
            } else {
                // Create a new StockWallet for this stock
                StockWallet newStockWallet = new StockWallet(stockSymbol, stockPrice, numShares, ownerUsername);
                stockWalletRepository.save(newStockWallet);
            }

            // Step 6: Deduct USD from FlatWallet balance
            flatWallet.setBalance(flatWallet.getBalance().subtract(totalPrice));
            flatWalletRepository.save(flatWallet);

            // Step 7: Return success response
            return ResponseEntity.ok("Successfully bought " + numShares + " shares of " + stockSymbol + " for $" + totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
            // Return error response
            return ResponseEntity.status(500).body("Error occurred while trying to buy stock: " + e.getMessage());
        }
    }


    public ResponseEntity<String> sellStock(String stockSymbol, BigDecimal numSharesToSell, String username) {
        try {
            // Step 1: Fetch stock price from external API
            BigDecimal stockPrice = fetchCurrentStockPrice(stockSymbol);

            // Step 2: Calculate total value for the stocks to sell
            BigDecimal totalUsdToCredit = stockPrice.multiply(numSharesToSell);

            // Step 3: Fetch and validate StockWallet
            StockWallet stockWallet = stockWalletRepository.findByOwnerUsernameAndStockSymbol(username, stockSymbol)
                    .orElseThrow(() -> new RuntimeException("StockWallet not found for stock '" + stockSymbol + "' and user: " + username));

            if (stockWallet.getNumShares().compareTo(numSharesToSell) < 0) {
                return ResponseEntity.badRequest().body("Insufficient shares in StockWallet for '" + stockSymbol + "'.");
            }

            // Step 4: Fetch user's FlatWallet
            FlatWallet flatWallet = flatWalletRepository.findByOwnerUsername(username)
                    .orElseThrow(() -> new RuntimeException("FlatWallet not found for user: " + username));

            // Step 5: Deduct shares from StockWallet
            stockWallet.setNumShares(stockWallet.getNumShares().subtract(numSharesToSell));

            // If no shares are left, delete the StockWallet entry; otherwise, save the update
            if (stockWallet.getNumShares().compareTo(BigDecimal.ZERO) == 0) {
                stockWalletRepository.delete(stockWallet);
            } else {
                stockWalletRepository.save(stockWallet);
            }

            // Step 6: Credit USD to FlatWallet
            flatWallet.setBalance(flatWallet.getBalance().add(totalUsdToCredit));
            flatWalletRepository.save(flatWallet);

            // Step 7: Return success response
            return ResponseEntity.ok("Successfully sold " + numSharesToSell + " shares of " + stockSymbol + " for $" + totalUsdToCredit);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Runtime exception: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while trying to sell stock: " + e.getMessage());
        }
    }


    private BigDecimal fetchCurrentStockPrice(String stockSymbol) throws IOException, InterruptedException {
        String apiUrl = FINNHUB_API_BASE_URL + "quote?symbol=" + stockSymbol + "&token=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-Finnhub-Token", API_KEY)
                .header("X-Finnhub-Secret", API_SECRET)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error fetching stock data for symbol '" + stockSymbol + "': " + response.body());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());
        BigDecimal stockPrice = jsonResponse.get("c").decimalValue(); // Current price ("c" field)

        if (stockPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("Invalid stock price for symbol: " + stockSymbol);
        }
        return stockPrice;
    }
}

