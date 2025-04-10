package com.example.demo.repository;

import com.example.demo.model.Team;
import com.example.demo.model.TeamStockHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamStockHoldingRepository extends JpaRepository<TeamStockHolding, Integer> {
    List<TeamStockHolding> findByTeam(Team team);
}
