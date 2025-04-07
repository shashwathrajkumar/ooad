package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Stock;

import org.springframework.beans.factory.annotation.Value;

import java.util.*;


@Service
public class StockService {
    @Value("${finnhub.api.key}")
    private static String finnhubApiKey;

    
    private static final String BASE_URL = "https://finnhub.io/api/v1/quote?symbol=%s&token=" + finnhubApiKey;

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
                Double currentPrice = (Double) response.get("c");
                stockList.add(new Stock(symbol, currentPrice));
            }
        } catch (Exception e) {
            stockList.add(new Stock(symbol, 0)); // Add with null price
        }
    }

    return stockList;
}

}
