package com.carbontrack.services;

import com.carbontrack.models.User;
import com.carbontrack.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // ✅ Register user
    public User register(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
        return repo.save(user);
    }

    // ✅ Login (fixed Optional handling)
    public User login(String email, String password) {

        Optional<User> optionalUser = repo.findByEmail(email);

        if (optionalUser.isPresent()) {
            User u = optionalUser.get();

            if (u.getPassword().equals(password)) {
                return u;
            }
        }

        return null;
    }

    // ✅ Find by ID
    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    // ✅ Find by email (safe)
    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }
}