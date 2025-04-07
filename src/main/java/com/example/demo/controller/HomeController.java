package com.example.demo.controller;

import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String teamName = userService.getTeamNameByUsername(username);
        model.addAttribute("stocks", stockService.getLiveStockPrices());
        model.addAttribute("teamName", teamName);

        return "home";
    }
}
