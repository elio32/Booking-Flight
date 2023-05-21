package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.FlightDTO;
import com.project.BookingFlight.model.entity.Flight;

import java.util.Date;
import java.util.List;

public interface FlightService {
    List<FlightDTO> getAllFlights();
    void deleteFlight(Long id);
    FlightDTO saveNewFlight(Flight flight);
    List<FlightDTO> findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode(String origin, String destination,
                                                                          Date departureDate, String airlineCode);
    FlightDTO updateFlight(Long id, Flight requestedFlight);
}
