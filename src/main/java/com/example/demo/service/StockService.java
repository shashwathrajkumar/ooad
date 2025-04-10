package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Stock;

import io.github.cdimascio.dotenv.Dotenv;


import java.util.*;


@Service
public class StockService {
    Dotenv dotenv = Dotenv.load();
    private String finnhubApiKey= dotenv.get("FIN_API");

    private final Map<String, Double> lastKnownPrices = new HashMap<>();
    
    private final String BASE_URL = "https://finnhub.io/api/v1/quote?symbol=%s&token=" + finnhubApiKey;

    private static final List<String> STOCK_SYMBOLS = Arrays.asList(
            "AAPL", "MSFT", "GOOGL", "AMD", "TSLA", "META", "NFLX", "NVDA", "JPM", "INTC"
    );

    public List<Stock> getLiveStockPrices() {
        RestTemplate restTemplate = new RestTemplate();
        List<Stock> stockList = new ArrayList<>();
    
        for (String symbol : STOCK_SYMBOLS) {
            String url = String.format(BASE_URL, symbol);
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                if (response != null && response.containsKey("c")) {
                    // Double currentPrice = (Double) response.get("c");
                    Object priceObj = response.get("c");
                    Double currentPrice = null;
                    if (priceObj instanceof Number) {
                        currentPrice = ((Number) priceObj).doubleValue();
                    }
                    if (currentPrice != null && currentPrice > 0) {
                        lastKnownPrices.put(symbol, currentPrice);
                        stockList.add(new Stock(symbol, currentPrice));
                        continue; // skip fallback logic
                    }
                }
            } catch (HttpClientErrorException e) {
                if (!e.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                    System.out.println("HTTP error for " + symbol + ": " + e.getStatusCode() + " - " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("General error for " + symbol + ": " + e.getMessage());
            }
        
            // Fallback: use last known price
            Double fallbackPrice = lastKnownPrices.get(symbol);
            if (fallbackPrice != null) {
                stockList.add(new Stock(symbol, fallbackPrice));
            }
        }
    
        return stockList;
    }
    
}
