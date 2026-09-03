package com.carbontrack.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "travel_options")
public class TravelOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mode;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private double calculatedCO2;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}