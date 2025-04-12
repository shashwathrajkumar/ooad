package com.example.demo.controller;

import com.example.demo.dto.PitchRequest;
import com.example.demo.dto.PitchResponseDTO;
import com.example.demo.model.Pitch;
import com.example.demo.repository.PitchRepository;
import com.example.demo.repository.StockEntityRepository;
import com.example.demo.model.StockEntity;
import com.example.demo.service.PitchService;

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

    // Removed duplicate declaration of pitchRepository

    @Autowired
    private StockEntityRepository stockEntityRepository;

    @PostMapping("/create")
    public String createPitch(@RequestBody PitchRequest pitchRequest) {
        Optional<StockEntity> stockOpt = stockEntityRepository.findBySymbol(pitchRequest.getStockSymbol());
        if (stockOpt.isEmpty()) {
            return "Stock not found";
        }

        Pitch pitch = new Pitch();
        pitch.setUserId(1); // Replace with actual logged-in user ID logic
        pitch.setTeamId(1); // Replace with actual team ID logic
        pitch.setStock(stockOpt.get());
        pitch.setAction(pitchRequest.getAction());
        pitch.setQty(pitchRequest.getQuantity());
        pitch.setPrice(pitchRequest.getPrice());
        pitch.setStatus("PENDING");
        pitch.setCreatedAt(LocalDateTime.now());

        pitchRepository.save(pitch);

        return "Pitch submitted successfully";
    }

    @Autowired
    private PitchRepository pitchRepository;
    
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
        System.out.println("Received userId: " + userId);
        // Fetch pending requests for users in the same team, excluding the current user
        List<Pitch> pitches = pitchService.getPendingRequestsForTeammates(userId);
        List<PitchResponseDTO> response = pitches.stream()
                .map(PitchResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
}


}
