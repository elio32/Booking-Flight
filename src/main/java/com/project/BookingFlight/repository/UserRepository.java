package com.project.BookingFlight.repository;

import com.project.BookingFlight.model.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp,Long> {
    Optional<UserApp> findByEmail(String email);
    Optional<UserApp> findByUsername(String username);
}
