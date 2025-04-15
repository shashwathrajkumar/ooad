package com.example.demo.dto;

public class LeaderboardEntryDTO {
    private Long userId;
    private String username;
    private int score;

    public LeaderboardEntryDTO(Long userId, String username, int score) {
        this.userId = userId;
        this.username = username;
        this.score = score;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int delta) {
        this.score += delta;
    }
}
