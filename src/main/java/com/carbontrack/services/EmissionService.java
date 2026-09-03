package com.carbontrack.services;

import com.carbontrack.models.Activity;
import com.carbontrack.models.EmissionRecord;
import com.carbontrack.models.User;
import com.carbontrack.repositories.EmissionRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmissionService {

    private final EmissionRecordRepository repo;

    public EmissionService(EmissionRecordRepository repo) {
        this.repo = repo;
    }

    // ✅ Calculate CO2 (Java 8 compatible)
    public double calculateCO2(Activity activity) {

        String c = activity.getCategory().toLowerCase();
        double v = activity.getValue();

        double result;

        switch (c) {
            case "car":
                result = v * 0.21;
                break;

            case "bus":
                result = v * 0.05;
                break;

            case "electricity":
                result = v * 0.82;
                break;

            case "bike":
            case "walk":
                result = 0;
                break;

            default:
                result = v * 0.10;
        }

        return result;
    }

    // ✅ Save emission record
    public EmissionRecord saveRecord(User user, Activity activity, double co2) {

        EmissionRecord rec = new EmissionRecord();
        rec.setUser(user);
        rec.setActivity(activity);
        rec.setCalculatedCO2(co2);
        rec.setDate(LocalDate.now());

        return repo.save(rec);
    }

    // ✅ Total emission (for dashboard card)
    public double getTotalEmission(User user) {

        return repo.findByUser(user)
                .stream()
                .mapToDouble(EmissionRecord::getCalculatedCO2)
                .sum();
    }

    // 🔥 NEW: Weekly emission (for graph)
    public double[] getWeeklyEmissions(User user) {

        double[] week = new double[7]; // Mon → Sun

        repo.findByUser(user)
                .forEach(r -> {
                    int dayIndex = r.getDate().getDayOfWeek().getValue() - 1;
                    week[dayIndex] += r.getCalculatedCO2();
                });

        return week;
    }
}