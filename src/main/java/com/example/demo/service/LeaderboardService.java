package com.example.demo.service;

import com.example.demo.dto.LeaderboardEntryDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

//main leaderboardService
@Service
public class LeaderboardService {

    @Autowired private PitchRepository pitchRepository;
    @Autowired private VoteRepository voteRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StockEntityRepository stockEntityRepository;

    public List<LeaderboardEntryDTO> getLeaderboard() {
        Map<Long, LeaderboardEntryDTO> leaderboard = new HashMap<>();

        List<Pitch> pitches = pitchRepository.findAll();

        for (Pitch pitch : pitches) {
            User pitcher = pitch.getUser();
            if (pitcher == null) continue;

            StockEntity stock = pitch.getStock();
            if (stock == null) continue;

            boolean success = false;
            if (pitch.getAction().equalsIgnoreCase("BUY")) {
                success = stock.getPrice() > pitch.getPrice();
            } else if (pitch.getAction().equalsIgnoreCase("SELL")) {
                success = stock.getPrice() < pitch.getPrice();
            }

            // 🧑‍🚀 Score the pitcher
            Long pitcherId = (long) pitcher.getId();
            leaderboard.putIfAbsent(pitcherId,
                    new LeaderboardEntryDTO(pitcherId, pitcher.getUsername(), 0));
            leaderboard.get(pitcherId).addScore(success ? 10 : -10);

            // 🗳️ Score the voters
            List<Vote> votes = voteRepository.findByPitchId(pitch.getId());
            for (Vote vote : votes) {
                User voter = userRepository.findById(vote.getUserId()).orElse(null);
                if (voter == null) continue;

                boolean votedAccept = vote.getVote().equalsIgnoreCase("ACCEPT");

                boolean correctVote = (success && votedAccept) || (!success && !votedAccept);

                Long voterId = (long) voter.getId();
                leaderboard.putIfAbsent(voterId,
                        new LeaderboardEntryDTO(voterId, voter.getUsername(), 0));
                leaderboard.get(voterId).addScore(correctVote ? 5 : -5);
            }
        }

        List<LeaderboardEntryDTO> leaderboardList = new ArrayList<>(leaderboard.values());
        leaderboardList.sort((a, b) -> Integer.compare(b.getScore(), a.getScore())); // Descending

        return leaderboardList;
    }
}
