package com.example.demo.controller;

import com.example.demo.dto.PitchRequest;
import com.example.demo.model.Pitch;
import com.example.demo.repository.PitchRepository;
import com.example.demo.repository.StockEntityRepository;
import com.example.demo.model.StockEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/pitch")
public class PitchController {

    @Autowired
    private PitchRepository pitchRepository;

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
}
