package com.carbontrack.repositories;

import com.carbontrack.models.CarbonBudget;
import com.carbontrack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarbonBudgetRepository extends JpaRepository<CarbonBudget, Long> {

    // ✅ Get budget for a specific user
    Optional<CarbonBudget> findByUser(User user);
}