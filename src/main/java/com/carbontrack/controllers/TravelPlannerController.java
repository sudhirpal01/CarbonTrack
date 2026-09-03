package com.carbontrack.controllers;

import com.carbontrack.models.User;
import com.carbontrack.services.TravelPlannerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/travel")
public class TravelPlannerController {

    private final TravelPlannerService service;

    public TravelPlannerController(TravelPlannerService service) {
        this.service = service;
    }

    // ✅ Show page
    @GetMapping
    public String page(HttpSession session) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        return "travel_planner";
    }

    // ✅ Calculate travel emission
    @PostMapping("/calculate")
    public String calculate(HttpSession session,
                            @RequestParam String mode,
                            @RequestParam double distance,
                            Model model) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        var result = service.calculateTravel(u, mode, distance);

        model.addAttribute("t", result);

        return "travel_planner";
    }
}