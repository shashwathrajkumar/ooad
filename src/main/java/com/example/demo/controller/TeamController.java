package com.example.demo.controller;

import com.example.demo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); 
        model.addAttribute("teams", teamRepository.findAll()); 
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam int teamId) {
        Team selectedTeam = teamRepository.findById(teamId).orElse(null);
        if (selectedTeam != null) {
            user.setTeam(selectedTeam);
            userRepository.save(user);
            return "redirect:/";
        }
        return "register";
    }

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam("teamId") int teamId, Model model) {
        model.addAttribute("dashboard", dashboardService.getDashboardData(teamId));
        return "dashboard"; // This refers to dashboard.html
    }
}
