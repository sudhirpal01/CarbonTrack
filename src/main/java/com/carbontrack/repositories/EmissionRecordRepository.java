package com.carbontrack.repositories;

import com.carbontrack.models.EmissionRecord;
import com.carbontrack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmissionRecordRepository extends JpaRepository<EmissionRecord, Long> {

    // ✅ Get all records for a user
    List<EmissionRecord> findByUser(User user);
}