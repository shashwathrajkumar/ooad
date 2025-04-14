package com.example.demo.controller;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.VoteRequest;
import com.example.demo.model.Vote;
import com.example.demo.repository.PitchRepository;
import com.example.demo.repository.VoteRepository;
import com.example.demo.service.PitchService;

@RestController
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PitchService pitchService;

    @PostMapping("/api/vote")
    public ResponseEntity<?> castVote(@RequestBody VoteRequest voteRequest) {
        Vote vote = new Vote();
        vote.setPitchId(voteRequest.getPitchId());
        vote.setUserId(voteRequest.getUserId());
        vote.setVote(voteRequest.getVote());
        vote.setCreatedAt(LocalDateTime.now());

        pitchService.processVoteAndCheckMajority(voteRequest.getPitchId(), voteRequest.getUserId(), voteRequest.getVote());
        return ResponseEntity.ok().build();
    }
}
