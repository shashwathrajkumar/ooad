package com.example.demo.controller;

import com.example.demo.dto.PitchRequest;
import com.example.demo.dto.PitchResponseDTO;
import com.example.demo.model.Pitch;
import com.example.demo.repository.PitchRepository;
import com.example.demo.repository.StockEntityRepository;
import com.example.demo.model.StockEntity;
// Ensure the correct package path for PitchService
import com.example.demo.service.PitchService; // Update this path if PitchService is in a different package
import com.example.demo.model.User; // Ensure this matches the actual package of the User class
import com.example.demo.repository.UserRepository; // Ensure this matches the actual package of the UserRepository interface

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/pitch")
public class PitchController {

    @Autowired
    private UserRepository userRepository;

    // Removed duplicate declaration of pitchRepository

    @Autowired
    private StockEntityRepository stockEntityRepository;
    
    @PostMapping("/create")
    public ResponseEntity<String> createPitch(@RequestBody PitchRequest pitchRequest, HttpSession session) {
        // 1. Get username from session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(401).body("User not logged in.");
        }

        // 2. Fetch the user
        User user = userRepository.findByUsername(username); // or use userService if preferred
        if (user == null) {
            return ResponseEntity.status(404).body("User not found.");
        }

        // 3. Fetch the stock
        Optional<StockEntity> stockOpt = stockEntityRepository.findBySymbol(pitchRequest.getStockSymbol());
        if (stockOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Stock not found.");
        }

        // 4. Create and save pitch
        Pitch pitch = new Pitch();
        pitch.setUserId((long) user.getId());
        pitch.setTeamId(user.getTeam().getId());
        pitch.setStock(stockOpt.get());
        pitch.setAction(pitchRequest.getAction());
        pitch.setQty(pitchRequest.getQuantity());
        pitch.setPrice(pitchRequest.getPrice());
        pitch.setStatus("PENDING");
        pitch.setCreatedAt(LocalDateTime.now());

        pitchRepository.save(pitch);
        return ResponseEntity.ok("Pitch submitted successfully.");
    }

    @Autowired
    private PitchRepository pitchRepository;

    // Removed unused VoteRepository field
    
    @GetMapping("/all")
    public ResponseEntity<List<PitchResponseDTO>> getAllPitches() {
        List<Pitch> pitches = pitchRepository.findAllByOrderByCreatedAtDesc();
        List<PitchResponseDTO> response = pitches.stream()
                .map(PitchResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Autowired
    private PitchService pitchService;

    @GetMapping("/pending")
    public ResponseEntity<List<PitchResponseDTO>> getPendingRequests(@RequestParam Long userId) {
        // Fetch pending requests for users in the same team, excluding the current user
        List<Pitch> pitches = pitchService.getPendingRequestsForTeammates(userId);
        List<PitchResponseDTO> response = pitches.stream()
                .map(PitchResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // @PostMapping("/api/vote")
    // public ResponseEntity<?> castVote(@RequestBody VoteRequest voteRequest) {
    //     Vote vote = new Vote();
    //     vote.setPitchId(voteRequest.getPitchId());
    //     vote.setUserId(voteRequest.getUserId());
    //     vote.setVote(voteRequest.getVote()); // "ACCEPT" or "REJECT"
    //     vote.setCreatedAt(LocalDateTime.now());

    //     voteRepository.save(vote);
    //     pitchService.processVoteAndCheckMajority(voteRequest.getPitchId(), voteRequest.getUserId(), voteRequest.getVote());
    //     return ResponseEntity.ok().build();
    // }



}
