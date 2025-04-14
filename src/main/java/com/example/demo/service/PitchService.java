package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repository.PitchRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.demo.model.Pitch; // Ensure this path matches the actual location of Pitch
import com.example.demo.model.Vote;

@Service
public class PitchService {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    public List<Pitch> getPendingRequestsForTeammates(Long userId) {
        // 1. Get team ID for the current user
        Long teamId = userRepository.findTeamIdByUserId(userId);

        // 2. Get teammate user IDs (excluding current user)
        List<Long> teammateIds = userRepository.findUserIdsByTeamIdAndExcludeUserId(teamId, userId);

        // 3. Get pending pitches from teammates
        List<Pitch> pendingPitches = pitchRepository.findPendingRequestsForUserIds(teammateIds);

        // 4. Filter out pitches the current user has already voted on
        return pendingPitches.stream()
                .filter(pitch -> !voteRepository.existsByPitchIdAndUserId(pitch.getId(), userId))
                .collect(Collectors.toList());

    }

    public void processVoteAndCheckMajority(Long pitchId, Long userId, String action) {
        // 1. Save the vote
        Vote vote = new Vote();
        vote.setPitchId(pitchId);
        vote.setUserId(userId);
        vote.setVote(action.toUpperCase()); // "ACCEPT" or "REJECT"
        vote.setCreatedAt(LocalDateTime.now());
        voteRepository.save(vote);

        // 2. Fetch the pitch
        Pitch pitch = pitchRepository.findById(pitchId).orElse(null);
        if (pitch == null || !"PENDING".equalsIgnoreCase(pitch.getStatus())) return;

        // 3. Get all team member IDs
        Long teamId = pitch.getTeamId();
        List<Long> teamMemberIds = userRepository.findUserIdsByTeamId(teamId);

        // 4. Get all votes on this pitch
        List<Vote> votes = voteRepository.findByPitchId(pitchId);

        // 5. Check if all team members have voted (excluding the one who created the pitch)
        Set<Long> votedUserIds = votes.stream()
                .map(Vote::getUserId)
                .collect(Collectors.toSet());

        List<Long> eligibleVoters = teamMemberIds.stream()
                .filter(id -> !id.equals(pitch.getUserId()))
                .collect(Collectors.toList());

        if (votedUserIds.containsAll(eligibleVoters)) {
            // 6. Tally votes
            long accepts = votes.stream().filter(v -> "ACCEPT".equalsIgnoreCase(v.getVote())).count();
            long rejects = votes.size() - accepts;

            // 7. Update pitch status
            if (accepts > rejects) {
                pitch.setStatus("ACCEPTED");
            } else {
                pitch.setStatus("REJECTED");
            }
            pitchRepository.save(pitch);
        }
    }

}
