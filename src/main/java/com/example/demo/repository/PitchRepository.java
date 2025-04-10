package com.example.demo.repository;

import com.example.demo.model.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PitchRepository extends JpaRepository<Pitch, Integer> {
    // You can add custom query methods here if needed later
}
