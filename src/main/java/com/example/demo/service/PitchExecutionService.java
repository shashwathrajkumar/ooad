package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//main pitchexecution service
@Service
@Transactional
public class PitchExecutionService {

    @Autowired
    private TeamStockHoldingRepository holdingRepository;

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private TeamRepository teamRepository;  // Add TeamRepository to fetch Team by teamId

    public void executePitch(Pitch pitch) {
        StockEntity stockEntity = pitch.getStock(); // using StockEntity directly
        Stock stock = stockEntity.toStock(); // convert StockEntity to Stock
        Long teamId = pitch.getTeamId();  // Get teamId from Pitch object
        Team team = teamRepository.findById(teamId.intValue()).orElseThrow(() -> new RuntimeException("Team not found"));

        double marketPrice = stockEntity.getPrice();
   
        TeamStockHolding holding = holdingRepository.findByTeamAndStock(team, stock);

        if (pitch.getAction().equalsIgnoreCase("BUY") && marketPrice <= pitch.getPrice()) {
            int qty = pitch.getQty();
            double cost = pitch.getPrice() * qty;

            if (holding != null) {
                int newQty = holding.getQuantity() + qty;
                double totalInvestment = holding.getBuyPrice() * holding.getQuantity() + cost;
                double newBuyPrice = totalInvestment / newQty;

                holding.setQuantity(newQty);
                holding.setBuyPrice(newBuyPrice);
            } else {
                holding = new TeamStockHolding();
                holding.setTeam(team);
                holding.setStock(stock);
                holding.setQuantity(qty);
                holding.setBuyPrice(pitch.getPrice());
            }

            holdingRepository.save(holding);
            pitch.setStatus("EXECUTED");
            pitchRepository.save(pitch);
        } else if (pitch.getAction().equalsIgnoreCase("SELL") && marketPrice >= pitch.getPrice()) {
            if (holding != null && holding.getQuantity() >= pitch.getQty()) {
                int remainingQty = holding.getQuantity() - pitch.getQty();

                if (remainingQty == 0) {
                    holdingRepository.delete(holding);
                } else {
                    holding.setQuantity(remainingQty);
                    holdingRepository.save(holding);
                }

                pitch.setStatus("EXECUTED");
                pitchRepository.save(pitch);
            } else {
                pitch.setStatus("REJECTED"); // insufficient qty
                pitchRepository.save(pitch);
            }
        }
    }
}
