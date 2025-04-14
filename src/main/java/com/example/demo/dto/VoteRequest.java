package com.example.demo.dto;

public class VoteRequest {
    private Long pitchId;
    private Long userId;
    private String vote; // "ACCEPT" or "REJECT"

    public VoteRequest() {
    }

    // All-args constructor
    public VoteRequest(Long pitchId, Long userId, String vote) {
        this.pitchId = pitchId;
        this.userId = userId;
        this.vote = vote;
    }

    // Getters and Setters
    public Long getPitchId() {
        return pitchId;
    }

    public void setPitchId(Long pitchId) {
        this.pitchId = pitchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
