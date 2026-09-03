package com.carbontrack.services;

import com.carbontrack.models.CarbonBudget;
import com.carbontrack.models.User;
import com.carbontrack.repositories.CarbonBudgetRepository;
import org.springframework.stereotype.Service;

@Service
public class CarbonBudgetService {

    private final CarbonBudgetRepository repo;

    public CarbonBudgetService(CarbonBudgetRepository repo) {
        this.repo = repo;
    }

    public CarbonBudget setBudget(User user, int month, double limit) {
        return repo.findByUser(user)
                .map(b -> {
                    b.setMonth(month);
                    b.setMonthlyLimit(limit);
                    return repo.save(b);
                })
                .orElseGet(() -> {
                    CarbonBudget b = new CarbonBudget();
                    b.setUser(user);
                    b.setMonth(month);
                    b.setMonthlyLimit(limit);
                    b.setUsed(0);
                    return repo.save(b);
                });
    }

    public CarbonBudget updateUsage(CarbonBudget b, double used) {
        b.setUsed(b.getUsed() + used);
        return repo.save(b);
    }

    // ✅ NEW METHOD (IMPORTANT for dashboard)
    public double getRemaining(User user) {
        return repo.findByUser(user)
                .map(b -> b.getMonthlyLimit() - b.getUsed())
                .orElse(0.0);
    }

    // ✅ Check if budget is exceeded
    public boolean isBudgetExceeded(User user) {
        return repo.findByUser(user)
                .map(b -> b.getUsed() > b.getMonthlyLimit())
                .orElse(false);
    }

    // ✅ Get current budget status
    public CarbonBudget getBudget(User user) {
        return repo.findByUser(user).orElse(null);
    }
}