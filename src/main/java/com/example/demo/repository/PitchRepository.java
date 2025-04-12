package com.example.demo.repository;

import com.example.demo.model.Pitch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PitchRepository extends JpaRepository<Pitch, Integer> {
    List<Pitch> findAllByOrderByCreatedAtDesc();
    @Query("SELECT p FROM Pitch p WHERE p.user.id IN :userIds AND p.status = 'PENDING'")
    List<Pitch> findPendingRequestsForUserIds(@Param("userIds") List<Long> userIds);

}
