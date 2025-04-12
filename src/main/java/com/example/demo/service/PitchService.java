package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repository.PitchRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.model.Pitch; // Ensure this path matches the actual location of Pitch

@Service
public class PitchService {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Pitch> getPendingRequestsForTeammates(Long userId) {
        // Get the team of the current user
        Long teamId = userRepository.findTeamIdByUserId(userId);

        // Get all user IDs in the same team, excluding the current user
        List<Long> teammateIds = userRepository.findUserIdsByTeamIdAndExcludeUserId(teamId, userId);

        return pitchRepository.findPendingRequestsForUserIds(teammateIds);

    }
}
