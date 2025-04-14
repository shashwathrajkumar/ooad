package com.example.demo.model;
import com.example.demo.repository.PitchRepository;
import com.example.demo.service.PitchExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PitchExecutionScheduler {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private PitchExecutionService executionService;

    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void executeAcceptedPitches() {
        List<Pitch> acceptedPitches = pitchRepository.findByStatus("ACCEPTED");

        for (Pitch pitch : acceptedPitches) {
            executionService.executePitch(pitch);
        }
    }
}
