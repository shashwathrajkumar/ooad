package com.example.demo.controller;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.VoteRequest;
import com.example.demo.model.Vote;
import com.example.demo.repository.VoteRepository;
// Ensure the correct package path for PitchService
import com.example.demo.service.PitchService; // Ensure this matches the actual package path of PitchService

@RestController
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PitchService pitchService; // Ensure PitchService is defined and annotated with @Service in its class

    @PostMapping("/api/vote")
    public ResponseEntity<?> castVote(@RequestBody VoteRequest voteRequest) {
        Vote vote = new Vote();
        vote.setPitchId(voteRequest.getPitchId());
        vote.setUserId(voteRequest.getUserId());
        vote.setVote(voteRequest.getVote());
        vote.setCreatedAt(LocalDateTime.now());

        pitchService.processVoteAndCheckMajority(voteRequest.getPitchId(), voteRequest.getUserId(), voteRequest.getVote());
        voteRepository.save(vote); // Save the vote to the database
        return ResponseEntity.ok().build();
    }
}
