package com.carbontrack.services;

import com.carbontrack.models.RewardPoints;
import com.carbontrack.models.User;
import com.carbontrack.repositories.RewardPointsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RewardService {

    private final RewardPointsRepository repo;

    public RewardService(RewardPointsRepository repo) {
        this.repo = repo;
    }

    // ✅ ADD POINTS (used in Activity + Challenge)
    public RewardPoints addPoints(User user, int pts) {

        RewardPoints r = new RewardPoints();
        r.setUser(user);
        r.setPoints(pts);
        r.setReason("Activity Reward");
        r.setDate(LocalDate.now());

        return repo.save(r);
    }

    // ✅ ADD REWARD WITH CUSTOM REASON
    public RewardPoints addReward(User user, int pts, String reason) {

        RewardPoints r = new RewardPoints();
        r.setUser(user);
        r.setPoints(pts);
        r.setReason(reason);
        r.setDate(LocalDate.now());

        return repo.save(r);
    }

    // ✅ GET TOTAL POINTS (used everywhere)
    public int getPoints(User user) {

        return repo.findByUser(user)
                .stream()
                .mapToInt(RewardPoints::getPoints)
                .sum();
    }
}