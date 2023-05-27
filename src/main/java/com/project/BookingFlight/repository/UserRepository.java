package com.project.BookingFlight.repository;

import com.project.BookingFlight.model.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp,Long> {
    Optional<UserApp> findByEmail(String email);
    Optional<UserApp> findByUsername(String username);
}
