package com.jt.securetrading.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.securetrading.models.wallets.CryptoWallet;
import com.jt.securetrading.models.wallets.FlatWallet;
import com.jt.securetrading.repositories.CryptoWalletRepository;
import com.jt.securetrading.repositories.FlatWalletRepository;
import com.jt.securetrading.services.CryptoWalletService;
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
import java.util.List;
import java.util.Optional;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {

    @Value("${coingecko.api.key}")
    private String API_KEY;

    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;

    @Autowired
    private FlatWalletRepository flatWalletRepository;

    @Override
    public ResponseEntity<String> getCoinInfo(String coinId) {
        try {
            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.coingecko.com/api/v3/coins/" + coinId))
                    .header("accept", "application/json")
                    .header("x-cg-demo-api-key", API_KEY) // Use an actual API key here.
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            // Send the HTTP request and get the response
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the API response status code is not 200 (OK)
            if (response.statusCode() != 200) {
                return ResponseEntity.status(response.statusCode())
                        .body("Error fetching data for coin '" + coinId + "': " + response.body());
            }

            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            String id = jsonResponse.path("id").asText();
            String name = jsonResponse.path("name").asText();
            String symbol = jsonResponse.path("symbol").asText();
            double currentPriceUsd = jsonResponse.path("market_data").path("current_price").path("usd").asDouble();

            // Format the result string
            String result = String.format("Coin Details: [ID: %s, Name: %s, Symbol: %s, Price in USD: $%.2f]", id, name, symbol, currentPriceUsd);

            return ResponseEntity.ok(result);

        } catch (IOException | InterruptedException e) {
            // Handle exceptions for HTTP client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching data: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions (e.g., JSON parsing errors)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> buyCrypto(String coinId, BigDecimal numCoins, String ownerUsername) throws IOException, InterruptedException {
        // Step 1: Fetch coin price from the API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.coingecko.com/api/v3/coins/" + coinId))
                .header("accept", "application/json")
                .header("x-cg-demo-api-key", API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return ResponseEntity.status(response.statusCode()).body("Error fetching data for coin '" + coinId + "': " + response.body());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());
        String coinName = jsonResponse.path("name").asText();
        String coinSymbol = jsonResponse.path("symbol").asText();
        double priceUsd = jsonResponse.path("market_data").path("current_price").path("usd").asDouble();

        if (priceUsd == 0) {
            return ResponseEntity.badRequest().body("Invalid price for cryptocurrency: " + coinId);
        }

        // Step 2: Calculate the total price in USD for the requested number of coins
        BigDecimal totalPriceUsd = BigDecimal.valueOf(priceUsd).multiply(numCoins);

        // Step 3: Fetch the user's FlatWallet
        FlatWallet flatWallet = flatWalletRepository.findByOwnerUsername(ownerUsername)
                .orElseThrow(() -> new RuntimeException("Flat wallet not found for user: " + ownerUsername));

        // Step 4: Check if FlatWallet has sufficient balance
        if (flatWallet.getBalance().compareTo(totalPriceUsd) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance in FlatWallet. Required: $" + totalPriceUsd);
        }

        // Step 5: Fetch or create a new CryptoWallet for the given coin
        Optional<CryptoWallet> existingWallet = cryptoWalletRepository.findByOwnerUsernameAndCoinName(ownerUsername, coinName);

        if (existingWallet.isPresent()) {
            // Increment numOfCoins for the existing wallet
            CryptoWallet cryptoWallet = existingWallet.get();
            cryptoWallet.setNumOfCoins(cryptoWallet.getNumOfCoins().add(numCoins));
            cryptoWalletRepository.save(cryptoWallet);
        } else {
            // Create a new wallet for this coin
            CryptoWallet newWallet = new CryptoWallet(coinSymbol, coinName, numCoins, ownerUsername);
            cryptoWalletRepository.save(newWallet);
        }

        // Step 6: Deduct USD from FlatWallet balance
        flatWallet.setBalance(flatWallet.getBalance().subtract(totalPriceUsd));
        flatWalletRepository.save(flatWallet);

        // Step 7: Return success response
        return ResponseEntity.ok("Successfully bought " + numCoins + " " + coinSymbol + " for $" + totalPriceUsd);
    }


    @Override
    public ResponseEntity<String> sellCrypto(String coinId, BigDecimal numCoinsToSell, String ownerUsername) throws IOException, InterruptedException {
        // Step 1: Fetch coin price from the API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.coingecko.com/api/v3/coins/" + coinId))
                .header("accept", "application/json")
                .header("x-cg-demo-api-key", API_KEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return ResponseEntity.status(response.statusCode())
                    .body("Error fetching data for coin '" + coinId + "': " + response.body());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());
        String coinName = jsonResponse.path("name").asText();
        double priceUsd = jsonResponse.path("market_data").path("current_price").path("usd").asDouble();

        if (priceUsd == 0) {
            return ResponseEntity.badRequest().body("Invalid price for cryptocurrency: " + coinId);
        }

        // Step 2: Calculate total USD value for the coins being sold
        BigDecimal totalUsdToCredit = BigDecimal.valueOf(priceUsd).multiply(numCoinsToSell);

        // Step 3: Fetch CryptoWallet for the user and specific coin
        CryptoWallet cryptoWallet = cryptoWalletRepository.findByOwnerUsernameAndCoinName(ownerUsername, coinName)
                .orElseThrow(() -> new RuntimeException("Crypto wallet not found for coin '" + coinName + "' and user: " + ownerUsername));

        // Step 4: Fetch FlatWallet of the user
        FlatWallet flatWallet = flatWalletRepository.findByOwnerUsername(ownerUsername)
                .orElseThrow(() -> new RuntimeException("Flat wallet not found for user: " + ownerUsername));

        // Step 5: Check if sufficient coins are available in CryptoWallet
        if (cryptoWallet.getNumOfCoins().compareTo(numCoinsToSell) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance in CryptoWallet for '" + coinName + "'.");
        }

        // Step 6: Deduct coins from CryptoWallet
        cryptoWallet.setNumOfCoins(cryptoWallet.getNumOfCoins().subtract(numCoinsToSell));

        // Step 7: If number of coins becomes 0 (or less), delete the CryptoWallet entry
        if (cryptoWallet.getNumOfCoins().compareTo(BigDecimal.ZERO) == 0) {
            cryptoWalletRepository.delete(cryptoWallet);
        } else {
            cryptoWalletRepository.save(cryptoWallet); // Save updated wallet only if coins remain
        }

        // Step 8: Credit USD amount to FlatWallet
        flatWallet.setBalance(flatWallet.getBalance().add(totalUsdToCredit));
        flatWalletRepository.save(flatWallet);

        // Step 9: Return success response
        return ResponseEntity.ok("Successfully sold " + numCoinsToSell + " " + coinName + " for $" + totalUsdToCredit);
    }

    @Override
    public List<CryptoWallet> getCryptoAssetsByUser(String username) {
        // Query the repository for wallets associated with the username
        return cryptoWalletRepository.findAllByOwnerUsername(username);
    }
}
