package com.example.demo.repository;
import java.util.Optional;
import com.example.demo.model.StockEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//main stockentity repo
@Repository
public interface StockEntityRepository extends JpaRepository<StockEntity, Integer> {
    Optional<StockEntity> findBySymbol(String symbol);
}
