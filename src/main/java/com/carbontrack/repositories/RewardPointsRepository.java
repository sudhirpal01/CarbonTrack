package com.carbontrack.repositories;

import com.carbontrack.models.RewardPoints;
import com.carbontrack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardPointsRepository extends JpaRepository<RewardPoints, Long> {

    // ✅ Get all reward records for a user
    List<RewardPoints> findByUser(User user);
}