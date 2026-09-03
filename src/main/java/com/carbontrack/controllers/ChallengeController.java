package com.carbontrack.controllers;

import com.carbontrack.models.DailyChallenge;
import com.carbontrack.models.User;
import com.carbontrack.services.ChallengeService;
import com.carbontrack.services.RewardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService service;
    private final RewardService rewardService;

    public ChallengeController(ChallengeService service,
                               RewardService rewardService) {
        this.service = service;
        this.rewardService = rewardService;
    }

    // ✅ Show challenges
    @GetMapping
    public String page(HttpSession session, Model model) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        model.addAttribute("list", service.getTodayChallenges());
        model.addAttribute("completedIds", service.getCompletedChallengeIds(u));

        return "challenges";
    }

    // ✅ Complete challenge
    @PostMapping("/complete")
    public String complete(HttpSession session,
                           @RequestParam Long challengeId) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        DailyChallenge c = service.getTodayChallenges()
                .stream()
                .filter(x -> x.getId().equals(challengeId))
                .findFirst()
                .orElse(null);

        if (c != null && !service.isCompleted(u, c)) {

            service.completeChallenge(u, c);
            rewardService.addReward(u, c.getPoints(), "Challenge completed");
        }

        return "redirect:/challenges";
    }
}