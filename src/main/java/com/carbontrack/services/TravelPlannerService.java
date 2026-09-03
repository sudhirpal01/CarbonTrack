package com.carbontrack.services;

import com.carbontrack.models.TravelOption;
import com.carbontrack.models.User;
import com.carbontrack.repositories.TravelOptionRepository;
import org.springframework.stereotype.Service;

@Service
public class TravelPlannerService {

    private final TravelOptionRepository repo;

    public TravelPlannerService(TravelOptionRepository repo) {
        this.repo = repo;
    }

    public TravelOption calculateTravel(User user, String mode, double distance) {

        double factor;

        switch (mode.toLowerCase()) {
            case "car":
                factor = 0.21;
                break;

            case "bus":
                factor = 0.05;
                break;

            case "bike":
            case "walk":
                factor = 0;
                break;

            default:
                factor = 0.1;
        }

        double co2 = distance * factor;

        TravelOption t = new TravelOption();
        t.setUser(user);
        t.setMode(mode.toUpperCase()); // ✅ consistency
        t.setDistance(distance);
        t.setCalculatedCO2(co2);

        return repo.save(t);
    }
}