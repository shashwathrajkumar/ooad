package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

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
}
