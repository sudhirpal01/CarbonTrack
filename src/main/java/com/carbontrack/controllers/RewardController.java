package com.carbontrack.controllers;

import com.carbontrack.models.User;
import com.carbontrack.services.RewardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/rewards")
    public String page(HttpSession session, Model model) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        // ✅ REAL DATA FROM DB
        model.addAttribute("points", rewardService.getPoints(u));

        return "rewards";
    }
}