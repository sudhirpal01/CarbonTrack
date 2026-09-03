package com.carbontrack.repositories;

import com.carbontrack.models.DailyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyChallengeRepository extends JpaRepository<DailyChallenge, Long> {

    // ✅ Get today's challenges
    List<DailyChallenge> findByDate(LocalDate date);
}