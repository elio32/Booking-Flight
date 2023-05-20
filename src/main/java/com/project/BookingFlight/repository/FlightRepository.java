package com.project.BookingFlight.repository;

import com.project.BookingFlight.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight,Long> {
    Flight findByOriginAndDestinationAndDepartureDateAndAirlineCode(String origin, String destination,
                                                                              Date departureDate,String airlineCode);
//    void delete(Optional<Flight> flight); // duhet ta shikoj se sma merr pa optional
    Flight findByOriginAndDestinationAndDepartureDate(String origin, String destination,
                                                                Date departureDate);
    Optional<Flight> findByFlightNumber(String flightNumber);
}
