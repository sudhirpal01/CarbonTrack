package com.carbontrack.services;

import com.carbontrack.models.Activity;
import com.carbontrack.models.User;
import com.carbontrack.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository repo;
    private final EmissionService emissionService;

    public ActivityService(ActivityRepository repo,
                           EmissionService emissionService) {
        this.repo = repo;
        this.emissionService = emissionService;
    }

    public Activity addActivity(User user, String category, double value) {

        Activity a = new Activity();
        a.setUser(user);
        a.setCategory(category);
        a.setValue(value);
        a.setDate(LocalDate.now());

        Activity saved = repo.save(a);

        double co2 = emissionService.calculateCO2(saved);
        emissionService.saveRecord(user, saved, co2);

        return saved;
    }

    public List<Activity> getUserActivities(User user) {
        return repo.findByUser(user);
    }
}