package com.carbontrack.controllers;

import com.carbontrack.models.DailyChallenge;
import com.carbontrack.models.User;
import com.carbontrack.services.CarbonBudgetService;
import com.carbontrack.services.ChallengeService;
import com.carbontrack.services.EmissionService;
import com.carbontrack.services.RewardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final EmissionService emissionService;
    private final CarbonBudgetService carbonBudgetService;
    private final RewardService rewardService;
    private final ChallengeService challengeService;

    public DashboardController(EmissionService emissionService,
                               CarbonBudgetService carbonBudgetService,
                               RewardService rewardService,
                               ChallengeService challengeService) {
        this.emissionService = emissionService;
        this.carbonBudgetService = carbonBudgetService;
        this.rewardService = rewardService;
        this.challengeService = challengeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // ✅ Fetch once
        double totalEmission = emissionService.getTotalEmission(user);

        // ✅ Add attributes
        model.addAttribute("totalEmission", totalEmission);
        model.addAttribute("budgetLeft", carbonBudgetService.getRemaining(user));
        model.addAttribute("points", rewardService.getPoints(user));
        model.addAttribute("isBudgetExceeded", carbonBudgetService.isBudgetExceeded(user));

        DailyChallenge active = challengeService.getTodayChallenges()
                .stream()
                .findFirst()
                .orElse(null);

        if (active == null) {
            model.addAttribute("activeChallenge", "No challenge for today");
        } else if (challengeService.isCompleted(user, active)) {
            model.addAttribute("activeChallenge", "Completed: " + active.getChallengeText());
        } else {
            model.addAttribute("activeChallenge", "Today's challenge: " + active.getChallengeText());
        }

        // ✅ Chart
        model.addAttribute("weeklyEmissions",
                emissionService.getWeeklyEmissions(user));
        model.addAttribute("user", user);

        // ✅ Smart suggestion (FIXED HERE)
        if (carbonBudgetService.isBudgetExceeded(user)) {
            double overBudget = Math.abs(carbonBudgetService.getRemaining(user));
            model.addAttribute("suggestion", "⚠️ Budget exceeded by " + String.format("%.2f", overBudget) + " kg CO₂! Reduce your emissions immediately.");
        } else if (totalEmission > 150) {
            model.addAttribute("suggestion", "Reduce car usage 🚗");
        } else if (totalEmission > 80) {
            model.addAttribute("suggestion", "Try public transport");
        } else {
            model.addAttribute("suggestion", "Great job! Keep it up 🌱");
        }

        return "dashboard";
    }
}