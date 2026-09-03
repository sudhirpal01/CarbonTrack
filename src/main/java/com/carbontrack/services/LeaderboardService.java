package com.carbontrack.services;

import com.carbontrack.models.LeaderboardEntry;
import com.carbontrack.models.User;
import com.carbontrack.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LeaderboardService {

    private final UserRepository repo;
    private final RewardService rewardService;

    public LeaderboardService(UserRepository repo,
                              RewardService rewardService) {
        this.repo = repo;
        this.rewardService = rewardService;
    }

    public List<LeaderboardEntry> getLeaderboard() {

        return repo.findAll()
                .stream()
                .map(user -> new LeaderboardEntry(user.getName(), rewardService.getPoints(user)))
                .sorted(Comparator.comparingInt(LeaderboardEntry::points).reversed())
                .toList();
    }
}