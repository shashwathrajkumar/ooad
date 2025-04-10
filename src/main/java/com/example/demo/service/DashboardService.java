package com.example.demo.service;

import com.example.demo.model.Team;
import com.example.demo.model.TeamStockHolding;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamStockHoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamStockHoldingRepository holdingRepository;

    public Map<String, Object> getDashboardData(int teamId) {
        Map<String, Object> dashboard = new HashMap<>();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<TeamStockHolding> holdings = holdingRepository.findByTeam(team);

        double currentValue = 0;
        double totalInvested = 0;

        List<Map<String, Object>> stockDetails = new ArrayList<>();

        for (TeamStockHolding holding : holdings) {
            double price = holding.getStock().getPrice(); // already saved by scheduled job
            int quantity = holding.getQuantity();
            double invested = holding.getTotalInvested();
            double value = price * quantity;
            double pnl = value - invested;

            currentValue += value;
            totalInvested += invested;

            Map<String, Object> stock = new HashMap<>();
            stock.put("symbol", holding.getStock().getSymbol());
            stock.put("quantity", quantity);
            stock.put("currentPrice", price);
            stock.put("invested", invested);
            stock.put("value", value);
            stock.put("pnl", pnl);

            stockDetails.add(stock);
        }

        dashboard.put("teamName", team.getName());
        dashboard.put("initialInvestment", team.getInitialInvestment());
        dashboard.put("totalInvested", totalInvested);
        dashboard.put("currentValue", currentValue);
        dashboard.put("holdings", stockDetails);

        return dashboard;
    }
}
