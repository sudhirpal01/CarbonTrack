package com.carbontrack.controllers;

import com.carbontrack.models.User;
import com.carbontrack.services.ActivityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService service;

    public ActivityController(ActivityService service){
        this.service = service;
    }

    // ✅ Show Add Activity Page
    @GetMapping("/add")
    public String addPage(HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";
        return "add_activity";
    }

    // ✅ Handle Form Submit
    @PostMapping("/add")
    public String add(HttpSession session,
                      @RequestParam String category,
                      @RequestParam double value) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        service.addActivity(u, category, value);


        return "redirect:/dashboard";
    }

    // ✅ Show Activity List
    @GetMapping("/list")
    public String list(HttpSession session, Model model) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        model.addAttribute("activities", service.getUserActivities(u));

        return "activities";
    }
}