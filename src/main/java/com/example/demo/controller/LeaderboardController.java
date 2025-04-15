package com.example.demo.controller;

import com.example.demo.dto.LeaderboardEntryDTO;
import com.example.demo.service.LeaderboardService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LeaderboardController {

    @Autowired private LeaderboardService leaderboardService;
    @Autowired private UserService userService;

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");

        model.addAttribute("username", username);
        model.addAttribute("teamName", userService.getTeamNameByUsername(username));
        model.addAttribute("teamId", userService.getTeamIdByUsername(username));

        List<LeaderboardEntryDTO> leaderboard = leaderboardService.getLeaderboard();
        model.addAttribute("leaderboard", leaderboard);

        return "leaderboard";
    }
}
