package com.carbontrack.services;

import com.carbontrack.models.DailyChallenge;
import com.carbontrack.models.User;
import com.carbontrack.models.UserChallengeRecord;
import com.carbontrack.repositories.DailyChallengeRepository;
import com.carbontrack.repositories.UserChallengeRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ChallengeService {

    private final DailyChallengeRepository challengeRepo;
    private final UserChallengeRecordRepository userRecordRepo;

    public ChallengeService(DailyChallengeRepository challengeRepo,
                            UserChallengeRecordRepository userRecordRepo) {
        this.challengeRepo = challengeRepo;
        this.userRecordRepo = userRecordRepo;
    }

    // ✅ Get today's challenges and generate a daily challenge if needed
    public List<DailyChallenge> getTodayChallenges() {
        LocalDate today = LocalDate.now();
        List<DailyChallenge> todayChallenges = challengeRepo.findByDate(today);

        if (!todayChallenges.isEmpty()) {
            return todayChallenges;
        }

        List<DailyChallenge> allChallenges = challengeRepo.findAll();
        if (allChallenges.isEmpty()) {
            DailyChallenge challenge1 = new DailyChallenge();
            challenge1.setChallengeText("Use public transport for one trip today");
            challenge1.setPoints(10);
            challenge1.setDate(today);

            DailyChallenge challenge2 = new DailyChallenge();
            challenge2.setChallengeText("Avoid using your car for one day");
            challenge2.setPoints(15);
            challenge2.setDate(today);

            DailyChallenge saved = challengeRepo.save(challenge1);
            challengeRepo.save(challenge2);
            return List.of(saved, challenge2);
        }

        DailyChallenge randomChallenge = allChallenges.get(
                ThreadLocalRandom.current().nextInt(allChallenges.size())
        );

        DailyChallenge todayChallenge = new DailyChallenge();
        todayChallenge.setChallengeText(randomChallenge.getChallengeText());
        todayChallenge.setPoints(randomChallenge.getPoints());
        todayChallenge.setDate(today);

        return List.of(challengeRepo.save(todayChallenge));
    }

    public List<Long> getCompletedChallengeIds(User user) {
        return userRecordRepo.findAllByUserAndDate(user, LocalDate.now())
                .stream()
                .map(record -> record.getChallenge().getId())
                .toList();
    }

    public boolean isCompleted(User user, DailyChallenge challenge) {
        return userRecordRepo
                .findByUserAndChallengeAndDate(user, challenge, LocalDate.now())
                .isPresent();
    }

    // ✅ Complete challenge (with duplicate check)
    public UserChallengeRecord completeChallenge(User user, DailyChallenge c) {

        // 🔥 Check if already completed today
        boolean alreadyDone = isCompleted(user, c);

        if (alreadyDone) {
            return null; // already completed
        }

        UserChallengeRecord r = new UserChallengeRecord();
        r.setUser(user);
        r.setChallenge(c);
        r.setCompleted(true);
        r.setDate(LocalDate.now());

        return userRecordRepo.save(r);
    }
}