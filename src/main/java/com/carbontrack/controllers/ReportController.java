package com.carbontrack.controllers;

import com.carbontrack.models.User;
import com.carbontrack.services.EmissionService;
import com.carbontrack.services.RewardService;
import com.carbontrack.utils.PDFUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final EmissionService emissionService;
    private final RewardService rewardService;

    public ReportController(EmissionService emissionService,
                            RewardService rewardService) {
        this.emissionService = emissionService;
        this.rewardService = rewardService;
    }

    @GetMapping("/report")
    public String generateReport(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        double totalCO2 = emissionService.getTotalEmission(user);
        int points = rewardService.getPoints(user);

        model.addAttribute("totalCO2", totalCO2);
        model.addAttribute("points", points);
        model.addAttribute("username", user.getName());

        return "report";
    }

    @GetMapping("/report/download")
    public ResponseEntity<byte[]> downloadReport(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, "/login")
                    .build();
        }

        double totalCO2 = emissionService.getTotalEmission(user);
        int points = rewardService.getPoints(user);

        byte[] pdfBytes = PDFUtil.generateMonthlyReportBytes(
                user.getName(),
                totalCO2,
                points
        );

        String filename = user.getName() + "_monthly_report.pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(pdfBytes);
    }
}