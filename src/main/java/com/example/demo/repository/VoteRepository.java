package com.example.demo.repository;

import com.example.demo.model.Vote;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByPitchIdAndUserId(Long pitchId, Long userId);
    List<Vote> findByPitchId(Long pitchId);
}
