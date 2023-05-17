package com.project.BookingFlight.repository;

import com.project.BookingFlight.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight,Long> {
    Optional<Flight> findByOriginAndDestinationAndDepartureDateAndAirlineCode(String origin, String destination,
                                                                              Date departureDate,String airlineCode);
}
