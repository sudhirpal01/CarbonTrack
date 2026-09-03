package com.carbontrack.controllers;

import com.carbontrack.models.User;
import com.carbontrack.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Home → redirect to login
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // ✅ Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ✅ Handle Login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User u = userService.login(email, password);

        if (u == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        session.setAttribute("user", u);

        return "redirect:/dashboard";
    }

    // ✅ Register Page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ✅ Handle Register
    @PostMapping("/register")
    public String register(User user, Model model) {

        user.setRole("USER");
        try {
            userService.register(user);
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    // ✅ Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}