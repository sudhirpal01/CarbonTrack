package com.carbontrack.repositories;

import com.carbontrack.models.User;
import com.carbontrack.models.DailyChallenge;
import com.carbontrack.models.UserChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserChallengeRecordRepository extends JpaRepository<UserChallengeRecord, Long> {

    // ✅ Check if user already completed a challenge today
    Optional<UserChallengeRecord> findByUserAndChallengeAndDate(
            User user,
            DailyChallenge challenge,
            LocalDate date
    );

    // ✅ Get all challenge completions for a user today
    List<UserChallengeRecord> findAllByUserAndDate(User user, LocalDate date);
}