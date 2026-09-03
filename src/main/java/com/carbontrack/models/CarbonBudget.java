package com.carbontrack.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carbon_budget")
public class CarbonBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private double monthlyLimit; 

    @Column(nullable = false)
    private double used = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}