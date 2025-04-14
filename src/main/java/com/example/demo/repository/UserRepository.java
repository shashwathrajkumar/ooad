package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Query("SELECT u.team.id FROM User u WHERE u.id = :userId")
    Long findTeamIdByUserId(@Param("userId") Long userId);
    @Query("SELECT u.id FROM User u WHERE u.team.id = :teamId AND u.id <> :userId")
    List<Long> findUserIdsByTeamIdAndExcludeUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
    @Query("SELECT u.id FROM User u WHERE u.team.id = :teamId")
    List<Long> findUserIdsByTeamId(@Param("teamId") Long teamId);
}

