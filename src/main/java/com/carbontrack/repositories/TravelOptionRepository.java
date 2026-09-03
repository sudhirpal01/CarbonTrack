package com.carbontrack.repositories;

import com.carbontrack.models.TravelOption;
import com.carbontrack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelOptionRepository extends JpaRepository<TravelOption, Long> {

    // ✅ Get all travel records for a user
    List<TravelOption> findByUser(User user);
}