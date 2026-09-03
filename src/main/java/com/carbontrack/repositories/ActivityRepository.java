package com.carbontrack.repositories;

import com.carbontrack.models.Activity;
import com.carbontrack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // ✅ Get activities for a specific user
    List<Activity> findByUser(User user);
}