package com.example.demo.controller;

import com.example.demo.model.StockEntity;
import com.example.demo.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockDataController {

    @Autowired
    private StockRepository stockRepository;

    @GetMapping("/api/stocks/all")
    public List<StockEntity> getAllStocks() {
        return stockRepository.findAll();
    }
}
