package com.example.demo.service;

import com.example.demo.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getLiveStockPrices() {
        RestTemplate restTemplate = new RestTemplate();
        List<Stock> stockList = new ArrayList<>();

        for (String symbol : STOCK_SYMBOLS) {
            String url = String.format(BASE_URL, symbol);
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                if (response != null && response.containsKey("c")) {
                    Double currentPrice = (Double) response.get("c");
                    if (currentPrice != null && currentPrice > 0) {
                        lastKnownPrices.put(symbol, currentPrice);
                        stockList.add(new Stock(symbol, currentPrice));
                        continue; // skip fallback logic
                    }
                }
            } catch (Exception e) {
                System.out.println("Error retrieving stock data for " + symbol + ": " + e.getMessage());
            }

            // Fallback: use last known price
            Double fallbackPrice = lastKnownPrices.get(symbol);
            if (fallbackPrice != null) {
                stockList.add(new Stock(symbol, fallbackPrice));
            }
        }

        return stockList;
    }
    @Scheduled(fixedRate = 300000) // 300,000 ms = 5 minutes
    public void updateStockPricesInDatabase() {
        System.out.println("[Scheduled] Updating stock prices in database...");
        List<Stock> livePrices = getLiveStockPrices();

        for (Stock liveStock : livePrices) {
            Optional<Stock> existing = stockRepository.findById(liveStock.getSymbol());
            if (existing.isPresent()) {
                Stock stock = existing.get();
                stock.setPrice(liveStock.getPrice());
                stockRepository.save(stock);
            }
        }
    }

    // 🧾 Utility method to get price from DB
    public double getPriceFromDatabase(String symbol) {
        return stockRepository.findById(symbol)
                .map(Stock::getPrice)
                .orElse(0.0);
    }

}
