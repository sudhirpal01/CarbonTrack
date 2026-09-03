package com.carbontrack.controllers;

import com.carbontrack.models.User;
import com.carbontrack.services.CarbonBudgetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final CarbonBudgetService service;

    public BudgetController(CarbonBudgetService service) {
        this.service = service;
    }

    // ✅ Show Budget Page
    @GetMapping
    public String page(HttpSession session, Model model) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        // Show current remaining budget
        model.addAttribute("remaining", service.getRemaining(u));

        return "budget";
    }

    // ✅ Set Monthly Budget
    @PostMapping("/set")
    public String set(HttpSession session,
                      @RequestParam int month,
                      @RequestParam double monthlyLimit) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        service.setBudget(u, month, monthlyLimit);

        return "redirect:/budget";
    }
}