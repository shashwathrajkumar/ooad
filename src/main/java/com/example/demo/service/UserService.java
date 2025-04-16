package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;


//main user service
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired // optional if using constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getTeamNameByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getTeam() != null) {
            return user.getTeam().getName();
        }
        return "No Team";
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public long getTeamIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getTeam() != null) {
            return user.getTeam().getId();
        }
        throw new RuntimeException("Team not found for user: " + username);
    }
}
